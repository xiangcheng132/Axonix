import cv2
from flask import Flask, jsonify, request
from ultralytics import YOLO
import torch
import threading
import time
import numpy as np
from collections import deque
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

class ObjectDetector:
    def __init__(self):
        self.cap = cv2.VideoCapture(0)  # 打开摄像头
        self.cap.set(cv2.CAP_PROP_FPS, 30)
        self.device = 'cuda' if torch.cuda.is_available() else 'cpu'
        self.model = YOLO("yolov8n.pt").to(self.device)
        self.running = False
        self.latest_results = []
        self.lock = threading.Lock()

        # 距离估计参数
        self.KNOWN_WIDTH = 20.0  # 物体真实宽度 (cm)
        self.FOCAL_LENGTH = 700  # 焦距 (像素)
        self.distance_deque = deque(maxlen=10)
        self.conf_deque = deque(maxlen=10)

    def calculate_distance(self, perceived_width):
        """计算物体距离"""
        if perceived_width > 0:
            return (self.KNOWN_WIDTH * self.FOCAL_LENGTH) / perceived_width
        return 0

    def smooth_distance(self, distance, confidence):
        """滑动窗口平滑距离"""
        self.distance_deque.append(distance)
        self.conf_deque.append(confidence)
        weighted = [d * c for d, c in zip(self.distance_deque, self.conf_deque)]
        return sum(weighted) / sum(self.conf_deque) if sum(self.conf_deque) > 0 else 0

    def detection_loop(self):
        """检测循环"""
        while self.running:
            ret, frame = self.cap.read()
            if not ret:
                continue

            results = self.model(frame)[0]
            detections = results.boxes.data.cpu().numpy()
            current_detections = []

            for detection in detections:
                x1, y1, x2, y2, confidence, class_id = detection[:6]
                if confidence > 0.4:
                    label = self.model.names[int(class_id)]
                    perceived_width = x2 - x1
                    distance = self.calculate_distance(perceived_width)
                    smoothed_distance = self.smooth_distance(distance, confidence)

                    current_detections.append({
                        "object": label,
                        "distance": round(smoothed_distance, 2),
                        "confidence": round(float(confidence), 2),
                        "position": {
                            "x1": int(x1),
                            "y1": int(y1),
                            "x2": int(x2),
                            "y2": int(y2)
                        }
                    })

            with self.lock:
                self.latest_results = current_detections

            time.sleep(0.1)

    def start(self):
        if not self.running:
            self.running = True
            self.thread = threading.Thread(target=self.detection_loop)
            self.thread.daemon = True
            self.thread.start()

    def stop(self):
        self.running = False
        if hasattr(self, "thread") and self.thread.is_alive():
            self.thread.join()
        self.cap.release()

detector = ObjectDetector()

@app.route('/start', methods=['POST'])
def start_detection():
    """启动检测"""
    detector.start()
    return jsonify({"status": "detection started"})

@app.route('/stop', methods=['POST'])
def stop_detection():
    """停止检测"""
    detector.stop()
    return jsonify({"status": "detection stopped"})

@app.route('/results', methods=['GET'])
def get_results():
    """获取检测结果"""
    try:
        print("Request received for /results")
        with detector.lock:
            if not detector.latest_results:
                return jsonify({"results": []})

            # 将 float32 转换为 float
            results_serializable = []
            for detection in detector.latest_results:
                detection["distance"] = float(detection["distance"])
                detection["confidence"] = float(detection["confidence"])
                detection["position"]["x1"] = int(detection["position"]["x1"])
                detection["position"]["y1"] = int(detection["position"]["y1"])
                detection["position"]["x2"] = int(detection["position"]["x2"])
                detection["position"]["y2"] = int(detection["position"]["y2"])
                results_serializable.append(detection)

            return jsonify({"results": results_serializable})

    except Exception as e:
        print(f"Error occurred: {e}")
        return jsonify({"error": str(e)}), 500



if __name__ == '__main__':
    try:
        app.run(host='0.0.0.0', port=5000, threaded=True)
    finally:
        detector.stop()
