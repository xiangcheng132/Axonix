from flask import Flask, request, jsonify
from ultralytics import YOLO
import cv2
import numpy as np
import base64
import io
from PIL import Image
import os
import time
from collections import deque

app = Flask(__name__)

# 加载模型
base_dir = os.path.dirname(__file__)
blind_model = YOLO(os.path.join(base_dir, 'lumos.pt'))  # 盲道模型
obstacle_model = YOLO(os.path.join(base_dir, 'yolo11n.pt'))  # 障碍物模型

# 将模型移动到相应设备
blind_model.to("cuda")
obstacle_model.to("cuda")

# 盲道参数
BOTTOM_WIDTH_RATIO = 0.5
TOP_WIDTH_RATIO = 0.25
TRAPEZOID_HEIGHT = 0.7

# 障碍物距离计算参数
KNOWN_WIDTH = 0.2          # 已知物体的实际宽度（米）
CALIBRATION_DISTANCE = 0.5 # 校准时的实际距离（米）
SMOOTHING_WINDOW = 5       # 平滑窗口大小

# 存储上一次的状态
last_status = None
last_tts_text = None

class DistanceCalculator:
    def __init__(self, window_size=5):
        self.width_buffer = deque(maxlen=window_size)
        self.focal_length = None

    def calibrate_focal_length(self, measured_pixel_width):
        self.focal_length = (measured_pixel_width * CALIBRATION_DISTANCE) / KNOWN_WIDTH

    def get_smoothed_width(self, current_width):
        self.width_buffer.append(current_width)
        return np.mean(self.width_buffer)

    def calculate_distance(self, perceived_width, image_center_x, box_center_x):
        if not self.focal_length:
            return None

        offset_ratio = abs(box_center_x - image_center_x) / image_center_x
        compensation = 1 + 0.15 * offset_ratio
        return (KNOWN_WIDTH * self.focal_length) / (perceived_width * compensation)

def get_trapezoid_bounds(h, w):
    bottom_width = w * BOTTOM_WIDTH_RATIO
    top_width = w * TOP_WIDTH_RATIO
    trapezoid_h = int(h * TRAPEZOID_HEIGHT)

    trapezoid = np.array([
        [(w - bottom_width) // 2, h],
        [(w + bottom_width) // 2, h],
        [(w + top_width) // 2, h - trapezoid_h],
        [(w - top_width) // 2, h - trapezoid_h]
    ], dtype=np.int32)

    boundaries = {
        'left': trapezoid[0][0],
        'right': trapezoid[1][0],
        'top_y': trapezoid[3][1]
    }

    return trapezoid, boundaries

def analyze_blind_box(box, trapezoid, boundaries):
    x, y, w, h = box
    points = [
        (x - w / 2, y - h / 2),
        (x + w / 2, y - h / 2),
        (x + w / 2, y + h / 2),
        (x - w / 2, y + h / 2)
    ]

    features = {
        'in_trapezoid': True,
        'left_out': False,
        'right_out': False,
        'upper_out': False,
        'partial_in': False
    }

    for (px, py) in points:
        if cv2.pointPolygonTest(trapezoid, (px, py), False) < 0:
            features['in_trapezoid'] = False

            if px < boundaries['left']:
                features['left_out'] = True
            elif px > boundaries['right']:
                features['right_out'] = True
            if py < boundaries['top_y']:
                features['upper_out'] = True
        else:
            features['partial_in'] = True

    return features

@app.route('/analyze', methods=['POST'])
def analyze_image():
    global last_status, last_tts_text

    # 初始化距离计算器
    distance_calc = DistanceCalculator(window_size=SMOOTHING_WINDOW)
    calibrated = False

    # 获取请求参数
    data = request.json
    if not data or 'image_base64' not in data:
        return jsonify({'code': 1, 'message': 'No image uploaded'}), 400

    try:
        # 解码 Base64 图像
        image_data = base64.b64decode(data['image_base64'])
        image = Image.open(io.BytesIO(image_data)).convert('RGB')
        frame = cv2.cvtColor(np.array(image), cv2.COLOR_RGB2BGR)
    except Exception as e:
        return jsonify({'code': 1, 'message': f'Error processing image: {str(e)}'}), 400

    h, w = frame.shape[:2]
    image_center_x = w // 2

    # 盲道检测
    trapezoid_pts, bounds = get_trapezoid_bounds(h, w)
    cv2.polylines(frame, [trapezoid_pts], True, (0, 255, 0), 2)

    # 障碍物检测
    obstacle_results = obstacle_model.predict(frame)[0]
    blind_results = blind_model.predict(frame)[0]

    # 盲道状态分析
    status_info = {
        'total': 0,
        'left_out': False,
        'right_out': False,
        'upper_out': False,
        'partial_in': False,
        'full_in': False
    }

    bounding_boxes = []
    obstacles = []

    # 处理盲道检测结果
    if blind_results.boxes is not None:
        boxes = blind_results.boxes.xywh.cpu().numpy()
        status_info['total'] = len(boxes)

        for box in boxes:
            features = analyze_blind_box(box, trapezoid_pts, bounds)
            status_info['left_out'] |= features['left_out']
            status_info['right_out'] |= features['right_out']
            status_info['upper_out'] |= features['upper_out']
            status_info['partial_in'] |= features['partial_in']
            status_info['full_in'] |= features['in_trapezoid']

            x, y, w_box, h_box = box
            bounding_boxes.append({
                'type': 'blind_path',
                'x': float(x - w_box / 2),
                'y': float(y - h_box / 2),
                'width': float(w_box),
                'height': float(h_box)
            })

    # 处理障碍物检测结果
    if obstacle_results.boxes is not None:
        boxes = obstacle_results.boxes.cpu().numpy()

        for box in boxes:
            x1, y1, x2, y2 = box.xyxy[0]
            cls_id = int(box.cls[0])
            label = obstacle_model.names[cls_id]

            current_width = x2 - x1
            box_center_x = (x1 + x2) // 2

            # 校准流程（首次检测时执行）
            if not calibrated and CALIBRATION_DISTANCE > 0:
                distance_calc.calibrate_focal_length(current_width)
                calibrated = True

            distance = None
            if distance_calc.focal_length:
                smoothed_width = distance_calc.get_smoothed_width(current_width)
                distance = distance_calc.calculate_distance(
                    smoothed_width, image_center_x, box_center_x
                )

            # 绘制障碍物框和距离
            if distance is not None:
                cv2.rectangle(frame, (int(x1), int(y1)), (int(x2), int(y2)), (0, 0, 255), 2)
                cv2.putText(frame, f"{label} {distance:.2f}m",
                            (int(x1), int(y1)-10),
                            cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 0, 255), 2)

            obstacles.append({
                'type': 'obstacle',
                'label': label,
                'x1': float(x1),
                'y1': float(y1),
                'x2': float(x2),
                'y2': float(y2),
                'distance': float(distance) if distance else None
            })

    # 盲道偏移判断
    status = "GO"
    tts_text = ""
    vibrate = False

    if status_info['total'] == 0:
        status = "no detection"
        tts_text = "未检测到盲道"
    else:
        if status_info['partial_in'] and status_info['left_out'] and status_info['upper_out']:
            status = "left!"
            tts_text = "向左调整"
        elif status_info['partial_in'] and status_info['right_out'] and status_info['upper_out']:
            status = "right!"
            tts_text = "向右调整"
        elif status_info['left_out'] and status_info['upper_out']:
            status = "warning:left!"
            tts_text = "严重左偏，请注意安全"
        elif status_info['right_out'] and status_info['upper_out']:
            status = "warning:right!"
            tts_text = "严重右偏，请注意安全"
        elif status_info['partial_in'] and status_info['left_out'] and not status_info['upper_out']:
            status = "turn left"
            tts_text = "前方盲道左转"
        elif status_info['partial_in'] and status_info['right_out'] and not status_info['upper_out']:
            status = "turn right"
            tts_text = "前方盲道右转"
        elif status_info['full_in'] and not (status_info['left_out'] or status_info['right_out'] or status_info['upper_out']):
            status = "stop"
            tts_text = "正前方盲道断裂"
        else:
            status = "GO"
            tts_text = "正常行进中"

    # 添加危险障碍物警告
    close_obstacles = [o for o in obstacles if o['distance'] and o['distance'] < 1.0]
    if close_obstacles:
        status = "warning:obstacle!"
        tts_text = f"前方{close_obstacles[0]['label']}，距离{close_obstacles[0]['distance']:.1f}米"
        vibrate = True

    # 只有当状态变化时才更新tts_text和vibrate
    response_data = {
        'code': 0,
        'message': 'Success',
        'status': status,
        'tts_text': '',
        'vibrate': False,
        'bounding_boxes': bounding_boxes,
        'obstacles': obstacles,
        'timestamp': time.strftime('%Y-%m-%d %H:%M:%S', time.localtime())
    }

    if last_status != status:
        last_status = status
        response_data['tts_text'] = tts_text
        response_data['vibrate'] = True
    else:
        response_data['vibrate'] = False

    # 添加文本到图像
    cv2.putText(frame, status, (20, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)

    # 转成 base64 返回图像结果
    _, buffer = cv2.imencode('.jpg',frame)
    jpg_as_text = base64.b64encode(buffer).decode('utf-8')
    response_data['image_annotated'] = jpg_as_text

    return jsonify(response_data)

if __name__ == "__main__":
    # 清理旧证书
    if os.path.exists("key.pem"):
        os.remove("key.pem")
    if os.path.exists("cert.pem"):
        os.remove("cert.pem")

    # 生成新证书
    subj_param = '/CN=0.0.0.0'  # 根据实际的ip地址修改
    os.system(f'openssl req -x509 -newkey rsa:4096 \
        -keyout key.pem -out cert.pem \
        -days 365 -nodes \
        -subj "{subj_param}"')

    # 启动服务器
    app.run(
        host='0.0.0.0',
        port=5001,
        ssl_context=('cert.pem', 'key.pem'),
        threaded=True,
        debug=False
    )