import tensorflow as tf
import tensorflow_hub as hub
import cv2
import numpy as np

# 加载 EfficientDet 模型
detector = hub.load("https://tfhub.dev/tensorflow/efficientdet/d4/1")

def detect_objects(frame):
    # 将 BGR 图像转换为 RGB
    rgb_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

    # 将图像转换为张量并调整大小
    input_tensor = tf.convert_to_tensor(rgb_frame, dtype=tf.float32)
    input_tensor = tf.image.resize(input_tensor, (512, 512))
    input_tensor = input_tensor[tf.newaxis, ...]  # 添加批次维度

    # 检测物体
    result = detector(input_tensor)
    return {key: value.numpy() for key, value in result.items()}

def main():
    # 打开摄像头
    cap = cv2.VideoCapture(0)

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        # 物体检测
        result = detect_objects(frame)

        # 可视化检测结果
        for i in range(int(result["num_detections"][0])):
            score = result["detection_scores"][0][i]
            if score < 0.5:  # 过滤低置信度
                continue
            bbox = result["detection_boxes"][0][i]
            class_id = int(result["detection_classes"][0][i])
            h, w, _ = frame.shape
            y1, x1, y2, x2 = int(bbox[0] * h), int(bbox[1] * w), int(bbox[2] * h), int(bbox[3] * w)
            cv2.rectangle(frame, (x1, y1), (x2, y2), (0, 255, 0), 2)
            cv2.putText(frame, f"Class {class_id} {score:.2f}", (x1, y1 - 10),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0), 2)

        # 显示结果
        cv2.imshow("EfficientDet Detection", frame)

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()

if __name__ == "__main__":
    main()