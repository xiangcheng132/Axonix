from flask import Flask, request, jsonify
from flask_cors import CORS
import base64
import cv2
import numpy as np
from ultralytics import YOLO
import os
import datetime

app = Flask(__name__)
CORS(app)  # 允许跨域请求

# 加载模型
base_dir = os.path.dirname(__file__)
model_path = os.path.join(base_dir, 'lumos.pt')
model = YOLO(model_path)

def draw_boxes(image, results):
    names = model.names
    boxes_info = []

    for result in results:
        for box in result.boxes:
            cls = int(box.cls[0])
            label = names[cls]
            conf = float(box.conf[0])
            x1, y1, x2, y2 = map(int, box.xyxy[0])
            boxes_info.append({
                "label": label,
                "confidence": round(conf, 2),
                "box": [x1, y1, x2, y2]
            })
            # 绘制矩形框与标签
            cv2.rectangle(image, (x1, y1), (x2, y2), (0, 255, 0), 2)
            cv2.putText(image, f"{label} {conf:.2f}", (x1, y1 - 10),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 255, 0), 2)
    return image, boxes_info

def analyze_blind_path(image):
    try:
        results = model(image)
        names = model.names
        status = "no detection"
        tts_text = "未检测到盲道"
        vibrate = False

        image, boxes_info = draw_boxes(image.copy(), results)

        for box in boxes_info:
            if box["label"] == "blind path":
                x1, _, x2, _ = box["box"]
                x_center = (x1 + x2) / 2
                width = image.shape[1]
                center_ratio = x_center / width

                if center_ratio < 0.3:
                    status = "warning:left!"
                    tts_text = "请立即向右调整"
                    vibrate = True
                elif center_ratio > 0.7:
                    status = "warning:right!"
                    tts_text = "请立即向左调整"
                    vibrate = True
                elif center_ratio < 0.45:
                    status = "left"
                    tts_text = "向右微调方向"
                    vibrate = True
                elif center_ratio > 0.55:
                    status = "right"
                    tts_text = "向左微调方向"
                    vibrate = True
                elif 0.45 <= center_ratio <= 0.55:
                    status = "stop"
                    tts_text = "当前方向正常"
                    vibrate = False
                else:
                    status = "GO"
                    tts_text = "可以继续前行"
                    vibrate = False
                break  # 只处理第一个检测到的盲道

        # 编码处理图像
        _, buffer = cv2.imencode('.png', image)
        image_base64 = base64.b64encode(buffer).decode()

        return {
            "code": 0,
            "message": "OK",
            "status": status,
            "tts_text": tts_text,
            "vibrate": vibrate,
            "image_annotated": image_base64,
            "bounding_boxes": boxes_info,
            "timestamp": datetime.datetime.now().isoformat()
        }

    except Exception as e:
        raise RuntimeError(f"分析失败: {str(e)}")

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

        # 解码图像
        image_data = base64.b64decode(data["image_base64"])
        nparr = np.frombuffer(image_data, np.uint8)
        image = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

        # 分析
        result = analyze_blind_path(image)
        return jsonify(result)

    except Exception as e:
        return jsonify({
            "code": 500,
            "message": f"Server error: {str(e)}",
            "status": "error"
        }), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
