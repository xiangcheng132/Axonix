import cv2
import torch
import numpy as np


# 加载YOLOv5模型
model = torch.hub.load('ultralytics/yolov5', 'yolov5s', pretrained=True)

# 已知物体的实际宽度（例如：20厘米）
KNOWN_WIDTH = 20.0  # 实际宽度（厘米）
FOCAL_LENGTH = 700  # 通过实验校准获得的焦距（像素）

# 距离测量函数
def calculate_distance(known_width, focal_length, perceived_width):
    return (known_width * focal_length) / perceived_width

# 打开摄像头
cap = cv2.VideoCapture(0)

while cap.isOpened():
    ret, frame = cap.read()
    if not ret:
        break

    # 使用YOLOv5模型检测物体
    results = model(frame)
    detections = results.xyxy[0].cpu().numpy()  # 获取检测结果框

    for detection in detections:
        x1, y1, x2, y2, confidence, class_id = detection[:6]
        label = model.names[int(class_id)]

        # 过滤低置信度检测
        if confidence > 0.3:
            # 计算边框宽度（像素宽度）
            perceived_width = x2 - x1

            # 计算距离
            distance = calculate_distance(KNOWN_WIDTH, FOCAL_LENGTH, perceived_width)

            # 显示检测结果
            cv2.rectangle(frame, (int(x1), int(y1)), (int(x2), int(y2)), (0, 255, 0), 2)
            text = f"{label}: {distance:.2f} cm"
            cv2.putText(frame, text, (int(x1), int(y1) - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 0), 2)

    # 显示结果帧
    cv2.imshow("Object Detection and Distance Estimation", frame)

    # 按 'q' 键退出
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()