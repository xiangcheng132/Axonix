from flask import Flask, jsonify
from flask_cors import CORS
from ultralytics import YOLO
import threading
import numpy as np
import torch
import cv2
import time
from collections import deque
import os

app = Flask(__name__)
CORS(app)

# 配置参数
KNOWN_WIDTH = 0.2
CALIBRATION_DISTANCE = 0.5
SMOOTHING_WINDOW = 5
TARGET_CLASSES = ['cell phone', 'traffic light', 'person', 'bicycle', 'car']

device = 'cuda' if torch.cuda.is_available() else 'cpu'


def calibrate_focal_length(measured_pixel_width):
    return (measured_pixel_width * CALIBRATION_DISTANCE) / KNOWN_WIDTH


def calculate_distance(perceived_width, focal_length, image_center_x, box_center_x):
    offset_ratio = abs(box_center_x - image_center_x) / image_center_x
    compensation = 1 + 0.15 * offset_ratio
    return (KNOWN_WIDTH * focal_length) / (perceived_width * compensation)


class DistanceCalculator:
    def __init__(self, window_size=5):
        self.width_buffer = deque(maxlen=window_size)
        self.focal_length = None

    def update_focal_length(self, measured_width):
        self.focal_length = calibrate_focal_length(measured_width)

    def get_smoothed_width(self, current_width):
        self.width_buffer.append(current_width)
        return np.mean(self.width_buffer)


class Detector:
    def __init__(self):
        self.model = YOLO("yolo11l.pt").to(device)
        self.cap = cv2.VideoCapture(0)
        self.cap.set(cv2.CAP_PROP_FRAME_WIDTH, 1080)
        self.cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 720)
        self.image_center_x = 1080 // 2

        self.calc = DistanceCalculator(SMOOTHING_WINDOW)
        self.calibrated = False
        self.running = False
        self.lock = threading.Lock()
        self.latest_results = []

    def detect_loop(self):
        while self.running:
            ret, frame = self.cap.read()
            if not ret:
                continue

            results = self.model.predict(source=frame, device=device, show=False, verbose=False)
            detections = []

            for result in results:
                boxes = result.boxes.cpu().numpy()
                for box in boxes:
                    x1, y1, x2, y2 = box.xyxy[0]
                    cls_id = int(box.cls[0])
                    label = self.model.names[cls_id]

                    if label not in TARGET_CLASSES:
                        continue

                    current_width = x2 - x1
                    box_center_x = (x1 + x2) // 2

                    if not self.calibrated:
                        self.calc.update_focal_length(current_width)
                        print(f"[INFO] Focal length calibrated: {self.calc.focal_length:.1f}")
                        self.calibrated = True

                    if self.calc.focal_length:
                        smoothed_width = self.calc.get_smoothed_width(current_width)
                        distance = calculate_distance(
                            smoothed_width, self.calc.focal_length,
                            self.image_center_x, box_center_x
                        )

                        detections.append({
                            "label": label,
                            "distance_m": round(float(distance), 2),
                            "position": {
                                "x1": int(x1), "y1": int(y1),
                                "x2": int(x2), "y2": int(y2)
                            }
                        })

            with self.lock:
                self.latest_results = detections

            time.sleep(0.1)

    def start(self):
        if not self.running:
            self.running = True
            self.thread = threading.Thread(target=self.detect_loop)
            self.thread.start()
            print("[INFO] Detection started...")

    def stop(self):
        self.running = False
        if hasattr(self, "thread") and self.thread.is_alive():
            self.thread.join()
        self.cap.release()
        print("[INFO] Detection stopped.")

    def get_results(self):
        with self.lock:
            return self.latest_results


# 实例化全局检测器
detector = Detector()


@app.route('/start', methods=['POST'])
def start_detection():
    detector.start()
    return jsonify({"status": "Detection started"})


@app.route('/results', methods=['GET'])
def get_results():
    return jsonify({"results": detector.get_results()})


@app.route('/stop', methods=['POST'])
def stop_detection():
    detector.stop()
    return jsonify({"status": "Detection stopped"})


if __name__ == "__main__":
    try:
        # 生成自签名证书（仅用于测试）
        os.system("openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -nodes -subj '/CN=localhost'")

        # 启动 HTTPS 服务器
        app.run(host="0.0.0.0", port=5000, threaded=True, ssl_context=('cert.pem', 'key.pem'))
    finally:
        detector.stop()
    