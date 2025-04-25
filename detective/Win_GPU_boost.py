from ultralytics import YOLO
import cv2
import numpy as np
from collections import deque
import torch

# 加载YOLO模型
model = YOLO("yolo11l.pt")
device = "cuda" if torch.cuda.is_available() else "cpu"
model.to(device)


# 配置参数
KNOWN_WIDTH = 0.2          # 已知物体的实际宽度（米）
CALIBRATION_DISTANCE = 0.5 # 校准时的实际距离（米）
SMOOTHING_WINDOW = 5       # 平滑窗口大小

# 焦距校准函数
def calibrate_focal_length(measured_pixel_width):
    return (measured_pixel_width * CALIBRATION_DISTANCE) / KNOWN_WIDTH

# 距离计算函数（带畸变补偿）
def calculate_distance(perceived_width, focal_length, image_center_x, box_center_x):
    # 简单的畸变补偿因子（根据目标在画面中的位置调整）
    offset_ratio = abs(box_center_x - image_center_x) / image_center_x
    compensation = 1 + 0.15 * offset_ratio  # 根据实际情况调整补偿系数
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

def main():
    cap = cv2.VideoCapture(0)
    W, H = 1080, 720  # 根据实际摄像头分辨率调整
    cap.set(cv2.CAP_PROP_FRAME_WIDTH, W)
    cap.set(cv2.CAP_PROP_FRAME_HEIGHT, H)

    # 初始化距离计算器
    calc = DistanceCalculator(window_size=SMOOTHING_WINDOW)
    calibrated = False
    image_center_x = W // 2

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        results = model.predict(source=frame, show=False, verbose=False)

        for result in results:
            boxes = result.boxes.cpu().numpy()
            for box in boxes:
                x1, y1, x2, y2 = box.xyxy[0]
                cls_id = int(box.cls[0])
                label = model.names[cls_id]

                # 只处理指定类别的物体（按需修改）
                if label not in ['cell phone', 'trafic light','person','bycycle','car']:
                    continue


                current_width = x2 - x1
                box_center_x = (x1 + x2) // 2

                # 校准流程（首次检测时执行）
                if not calibrated and CALIBRATION_DISTANCE > 0:
                    calc.update_focal_length(current_width)
                    print(f"Focal length calibrated: {calc.focal_length:.1f}")
                    calibrated = True

                if calc.focal_length:
                    # 使用平滑后的宽度值
                    smoothed_width = calc.get_smoothed_width(current_width)

                    # 计算带补偿的距离
                    distance = calculate_distance(
                        smoothed_width,
                        calc.focal_length,
                        image_center_x,
                        box_center_x
                    )

                    # 绘制结果
                    label = f"{label} {distance:.2f}m"
                    cv2.rectangle(frame, (int(x1), int(y1)), (int(x2), int(y2)), (0,255,0), 2)
                    cv2.putText(frame, label, (int(x1), int(y1)-10),
                                cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0,255,255), 2)

        cv2.imshow("Enhanced Distance Measurement", frame)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()

if __name__ == "__main__":
    main()
#uvicorn xuexi:app --reload

