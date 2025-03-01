import cv2
from flask import Flask, jsonify
from ultralytics import YOLO
import torch
import threading
import time

app = Flask(__name__)

class ObjectDetector:
    def __init__(self):
        # 初始化摄像头和模型
        self.cap = cv2.VideoCapture(0)
        self.device = 'mps' if torch.backends.mps.is_available() else 'cpu'
        self.model = YOLO('yolov5m.pt').to(self.device)

        # 距离测量参数
        self.KNOWN_WIDTH = 20.0  # 实际物体宽度（厘米）
        self.FOCAL_LENGTH = 700  # 校准焦距（像素）

        # 状态控制
        self.running = False
        self.latest_results = []
        self.lock = threading.Lock()

    def calculate_distance(self, perceived_width):
        """计算物体距离"""
        return (self.KNOWN_WIDTH * self.FOCAL_LENGTH) / perceived_width

    def detection_loop(self):
        """持续检测循环"""
        while self.running:
            ret, frame = self.cap.read()
            if not ret:
                continue

            # 预处理帧
            resized_frame = cv2.resize(frame, (640, 640))
            frame_tensor = torch.from_numpy(resized_frame).to(self.device).float() / 255.0
            frame_tensor = frame_tensor.permute(2, 0, 1).unsqueeze(0)

            # 执行检测
            results = self.model(frame_tensor)
            detections = results[0].boxes.data.cpu().numpy()

            # 处理检测结果
            current_detections = []
            original_height, original_width = frame.shape[:2]
            x_scale = original_width / 640
            y_scale = original_height / 640

            for detection in detections:
                x1, y1, x2, y2, confidence, class_id = detection[:6]
                if confidence > 0.4:  # 置信度阈值
                    perceived_width = (x2 - x1) * x_scale
                    distance = self.calculate_distance(perceived_width)

                    current_detections.append({
                        "object": self.model.names[int(class_id)],
                        "distance": round(float(distance), 2),
                        "confidence": round(float(confidence), 2),
                        "position": {
                            "x1": int(x1 * x_scale),
                            "y1": int(y1 * y_scale),
                            "x2": int(x2 * x_scale),
                            "y2": int(y2 * y_scale)
                        }
                    })

            # 更新最新结果
            with self.lock:
                self.latest_results = current_detections

            time.sleep(0.1)  # 控制检测频率

    def start(self):
        """启动检测线程"""
        if not self.running:
            self.running = True
            self.thread = threading.Thread(target=self.detection_loop)
            self.thread.start()

    def stop(self):
        """停止检测"""
        self.running = False
        if self.thread.is_alive():
            self.thread.join()
        self.cap.release()

# 全局检测器实例
detector = ObjectDetector()

@app.route('/start', methods=['POST'])
def start_detection():
    """启动检测接口"""
    detector.start()
    return jsonify({"status": "detection started"})

@app.route('/results')
def get_results():
    """获取最新结果接口"""
    with detector.lock:
        return jsonify({"results": detector.latest_results})

@app.route('/stop', methods=['POST'])
def stop_detection():
    """停止检测接口"""
    detector.stop()
    return jsonify({"status": "detection stopped"})

if __name__ == '__main__':
    try:
        app.run(host='0.0.0.0', port=5000, threaded=True)
    finally:
        detector.stop()