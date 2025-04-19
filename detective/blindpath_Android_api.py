from flask import Flask, request, jsonify
from flask_cors import CORS
import base64
import cv2
import numpy as np
import time
import threading
from ultralytics import YOLO
import os

app = Flask(__name__)
CORS(app)

# 初始化模型
base_dir = os.path.dirname(__file__)
model_path = os.path.join(base_dir, 'lumos.pt')
model = YOLO(model_path)

def get_timestamp():
    return time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())

def analyze_blind_path(image):
    """
    检测盲道，并给出状态、播报内容、是否震动、边界框
    """
    results = model(image)
    status = "no detection"
    tts_text = "未识别到盲道"
    vibrate = False
    boxes_output = []

    for result in results:
        for box in result.boxes:
            cls = int(box.cls[0])
            conf = float(box.conf[0])
            label = model.names[cls]
            x1, y1, x2, y2 = map(int, box.xyxy[0].tolist())
            x_center = (x1 + x2) / 2
            width = image.shape[1]
            ratio = x_center / width

            # 状态判断逻辑
            if label == "blind path":
                boxes_output.append({
                    "label": label,
                    "confidence": round(conf, 2),
                    "box": [x1, y1, x2, y2]
                })
                if ratio < 0.3:
                    status = "warning:left!"
                    tts_text = "请向右调整方向"
                    vibrate = True
                elif ratio > 0.7:
                    status = "warning:right!"
                    tts_text = "请向左调整方向"
                    vibrate = True
                elif 0.4 < ratio < 0.6:
                    status = "stop"
                    tts_text = "当前方向正常"
                    vibrate = False
                else:
                    status = "GO"
                    tts_text = "可继续前行"
                    vibrate = False

    return status, tts_text, vibrate, boxes_output, results

@app.route("/analyze", methods=["POST"])
def analyze():
    try:
        data = request.get_json()
        if not data or "image_base64" not in data:
            return jsonify({
                "code": 400,
                "message": "Invalid input: image_base64 is missing.",
                "status": "error"
            }), 400

        # 解码 base64
        image_data = base64.b64decode(data["image_base64"])
        nparr = np.frombuffer(image_data, np.uint8)
        image = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

        # 模型分析
        status, tts_text, vibrate, boxes_output, results = analyze_blind_path(image)

        # 可视化结果图
        annotated_image = results[0].plot()
        _, buffer = cv2.imencode('.png', annotated_image)
        annotated_base64 = base64.b64encode(buffer).decode("utf-8")

        # 响应返回
        return jsonify({
            "code": 0,
            "message": "OK",
            "status": status,
            "tts_text": tts_text,
            "vibrate": vibrate,
            "bounding_boxes": boxes_output,
            "image_annotated": annotated_base64,
            "timestamp": get_timestamp()
        }), 200

    except Exception as e:
        return jsonify({
            "code": 500,
            "message": f"Server error: {str(e)}",
            "status": "error"
        }), 500


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=False, threaded=True)
