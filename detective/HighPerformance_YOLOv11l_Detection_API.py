import cv2
import numpy as np
import base64
import torch
import logging
import time
import threading
import subprocess
from flask import Flask, request, jsonify
from flask_cors import CORS
from flask_limiter import Limiter
from flask_limiter.util import get_remote_address
from concurrent.futures import ThreadPoolExecutor
from ultralytics import YOLO
from collections import deque

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})

limiter = Limiter(
    app=app,
    key_func=get_remote_address,
    default_limits=["50 per minute"],
    storage_uri="memory://"
)

EXECUTOR = ThreadPoolExecutor(max_workers=2)
logger = logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)

# 配置参数
KNOWN_WIDTH = 0.2  # 已知物体的实际宽度（米）
CALIBRATION_DISTANCE = 0.5  # 校准时的实际距离（米）
SMOOTHING_WINDOW = 5  # 平滑窗口大小
SUPPORTED_CLASSES = {
    'person': '人',
    'car': '汽车',
    'bicycle': '自行车',
    'cell phone': '手机',
    'traffic light': '交通信号灯'
}


class DistanceCalculator:
    def __init__(self, window_size=SMOOTHING_WINDOW):
        self.width_buffer = deque(maxlen=window_size)
        self.focal_length = None

    def update_focal_length(self, measured_width):
        self.focal_length = (measured_width * CALIBRATION_DISTANCE) / KNOWN_WIDTH

    def get_smoothed_width(self, current_width):
        self.width_buffer.append(current_width)
        return np.mean(self.width_buffer)


class HighPerformanceDetector:
    def __init__(self):
        self.device = 'cuda' if torch.cuda.is_available() else 'cpu'
        logger.info(f"Initializing detector on {self.device.upper()}")

        # 模型初始化
        self.model = YOLO("yolo11l.pt").to(self.device)
        self.model.conf = 0.65
        self.model.iou = 0.45

        # 性能优化
        self.model.fuse()
        self.model.eval()
        self.half = True
        self.imgsz = 640

        # 状态监控
        self.latest_results = []
        self.processing_times = []
        self.lock = threading.Lock()
        torch.backends.cudnn.benchmark = True

        self.distance_calculator = DistanceCalculator()
        self.calibrated = False
        self.last_calibration_time = time.time()

    def _safe_decode(self, base64_str: str) -> np.ndarray:
        """优化图像解码"""
        try:
            if base64_str.startswith('data:image'):
                base64_str = base64_str.split(',', 1)[1]

            img_bytes = base64.b64decode(base64_str, validate=True)
            img = cv2.imdecode(np.frombuffer(img_bytes, np.uint8), cv2.IMREAD_COLOR)

            # 尺寸优化
            if max(img.shape[:2]) > 1280:
                h, w = img.shape[:2]
                scale = 1280 / max(h, w)
                img = cv2.resize(img, (int(w * scale), int(h * scale)))
            return img
        except Exception as e:
            logger.error(f"解码失败: {str(e)}")
            return None

    def process_frame(self, base64_str: str) -> dict:
        start = time.time()
        frame = None

        try:
            frame = self._safe_decode(base64_str)
            if frame is None:
                return {"error": "INVALID_IMAGE"}

            # 模型推理
            with torch.no_grad(), torch.amp.autocast(device_type='cuda', dtype=torch.float16):
                results = self.model(
                    source=frame,
                    imgsz=self.imgsz,
                    half=self.half,
                    max_det=5,
                    verbose=False
                )

            detections = []
            frame_w = frame.shape[1]
            image_center_x = frame_w // 2

            for result in results:
                for box in result.boxes.cpu().numpy():
                    label = self.model.names[int(box.cls[0])]
                    if label not in SUPPORTED_CLASSES:
                        continue

                    # 解析坐标
                    x1, y1, x2, y2 = map(int, box.xyxy[0])
                    x_center = (x1 + x2) // 2

                    # 计算距离
                    box_width = x2 - x1
                    current_width = box_width

                    # 校准流程（首次检测或每30秒重新校准）
                    if not self.calibrated or (time.time() - self.last_calibration_time) > 30:
                        self.distance_calculator.update_focal_length(current_width)
                        self.calibrated = True
                        self.last_calibration_time = time.time()
                        logger.info(f"Focal length calibrated: {self.distance_calculator.focal_length:.1f}")

                    if self.distance_calculator.focal_length:
                        # 使用平滑后的宽度值
                        smoothed_width = self.distance_calculator.get_smoothed_width(current_width)

                        # 简单的畸变补偿因子（根据目标在画面中的位置调整）
                        offset_ratio = abs(x_center - image_center_x) / image_center_x
                        compensation = 1 + 0.12 * offset_ratio  # 根据实际情况调整补偿系数
                        distance = (KNOWN_WIDTH * self.distance_calculator.focal_length) / (
                                smoothed_width * compensation)
                        distance = max(distance, 0.3)  # 最小0.3米

                        # 构建检测结果
                        detection = {
                            "label": SUPPORTED_CLASSES[label],
                            "distance": round(distance, 1),
                            "direction": "左前方" if x_center < frame_w * 0.35 else "右前方" if x_center > frame_w * 0.65 else "正前方",
                            "position": {"x1": x1, "y1": y1, "x2": x2, "y2": y2},
                            "timestamp": int(time.time() * 1000),
                            "center_x": x_center,
                            "width": box_width
                        }
                        detections.append(detection)

            # 更新状态
            with self.lock:
                self.latest_results = detections
                self.processing_times.append(time.time() - start)
                if len(self.processing_times) > 10:
                    self.processing_times.pop(0)

            logger.info(f"处理完成，耗时: {(time.time() - start) * 1000:.1f}ms")
            return {"success": True, "objects": detections}

        except Exception as e:
            logger.error(f"处理异常: {str(e)}")
            return {"error": "PROCESS_ERROR"}
        finally:
            if frame is not None:
                del frame


detector = HighPerformanceDetector()


@app.route('/detect', methods=['POST'])
@limiter.limit("30/minute")
def handle_detection():
    if not request.is_json:
        return jsonify({"error": "INVALID_FORMAT"}), 400

    data = request.get_json()
    if 'image' not in data:
        return jsonify({"error": "MISSING_IMAGE"}), 400

    try:
        future = EXECUTOR.submit(detector.process_frame, data['image'])
        return jsonify(future.result(timeout=2.0))
    except TimeoutError:
        return jsonify({"error": "TIMEOUT"}), 408
    except Exception as e:
        logger.error(f"服务器错误: {str(e)}")
        return jsonify({"error": "SERVER_ERROR"}), 500


@app.route('/status', methods=['GET'])
def get_status():
    mem_info = ""
    if torch.cuda.is_available():
        used = torch.cuda.memory_allocated() / 1024 ** 3
        total = torch.cuda.get_device_properties(0).total_memory / 1024 ** 3
        mem_info = f"{used:.1f}/{total:.1f}GB"

    return jsonify({
        "gpu_memory": mem_info,
        "pending_tasks": EXECUTOR._work_queue.qsize(),
        "avg_process_ms": f"{np.mean(detector.processing_times) * 1000:.1f}" if detector.processing_times else "0",
        "calibrated": detector.calibrated,
        "supported_classes": list(SUPPORTED_CLASSES.keys())
    })


def generate_self_signed_certificate():
    try:
        # 执行 OpenSSL 命令生成自签名证书
        command = [
            'openssl', 'req', '-x509', '-newkey', 'rsa:4096',
            '-keyout', 'key.pem', '-out', 'cert.pem',
            '-days', '365', '-nodes', '-subj', '/CN=localhost'
        ]
        subprocess.run(command, check=True)
        logger.info("自签名证书生成成功")
    except subprocess.CalledProcessError as e:
        logger.error(f"生成自签名证书时出错: {e}")
    except FileNotFoundError:
        logger.error("未找到 OpenSSL 命令，请确保 OpenSSL 已安装并配置到系统路径中")


if __name__ == '__main__':
    # 生成自签名证书
    generate_self_signed_certificate()

    # 改进的模型预热
    logger.info("模型预热中...")
    dummy_input = torch.rand(1, 3, 640, 640).to(detector.device)
    if detector.half:
        dummy_input = dummy_input.half()
    else:
        dummy_input = dummy_input.float() / 255.0

    with torch.no_grad():
        _ = detector.model(dummy_input)

    app.run(
        host="0.0.0.0",
        port=5000,
        ssl_context=('cert.pem', 'key.pem'),
        threaded=True
    )