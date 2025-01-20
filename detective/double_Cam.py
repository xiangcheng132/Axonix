import cv2
import numpy as np
from ultralytics import YOLO  # 确保 ultralytics 包安装 (pip install ultralytics)

# 双目摄像头的参数（需根据实际标定结果替换）
camera_matrix_left = np.array([[700, 0, 320], [0, 700, 240], [0, 0, 1]])
camera_matrix_right = np.array([[700, 0, 320], [0, 700, 240], [0, 0, 1]])
dist_coeffs_left = np.zeros(5)  # 假设畸变系数为零
dist_coeffs_right = np.zeros(5)
baseline = 0.1  # 双目摄像头的基线距离（单位：米）
focal_length = 700  # 摄像头焦距（单位：像素）

# 加载 YOLO 模型
model = YOLO("yolov8n.pt")  # 替换为您需要的 YOLO 模型文件

def compute_depth(disparity, baseline, focal_length):
    """计算深度"""
    with np.errstate(divide='ignore'):  # 忽略除零警告
        depth = (baseline * focal_length) / disparity
    depth[disparity <= 0] = 0  # 避免无效深度
    return depth

def main():
    # 打开双目摄像头
    cap_left = cv2.VideoCapture(0)  # 左摄像头
    cap_right = cv2.VideoCapture(0)  # 右摄像头

    if not cap_left.isOpened() or not cap_right.isOpened():
        print("无法打开双目摄像头")
        return

    while True:
        # 捕获左右图像帧
        ret_left, frame_left = cap_left.read()
        ret_right, frame_right = cap_right.read()

        if not ret_left or not ret_right:
            print("无法捕获图像")
            break

        # 转换为灰度图（立体匹配需要灰度图）
        gray_left = cv2.cvtColor(frame_left, cv2.COLOR_BGR2GRAY)
        gray_right = cv2.cvtColor(frame_right, cv2.COLOR_BGR2GRAY)

        # 计算视差图
        stereo = cv2.StereoBM_create(numDisparities=16, blockSize=15)
        disparity = stereo.compute(gray_left, gray_right).astype(np.float32) / 16.0

        # 计算深度图
        depth_map = compute_depth(disparity, baseline, focal_length)

        # 使用 YOLO 检测物体
        results = model(frame_left)

        # 绘制检测结果
        for result in results[0].boxes:
            # 获取边界框和类别
            x1, y1, x2, y2 = map(int, result.xyxy[0])
            class_id = int(result.cls[0])
            conf = float(result.conf[0])
            label = f"{model.names[class_id]} {conf:.2f}"

            # 计算物体的平均深度
            object_depth = np.mean(depth_map[y1:y2, x1:x2])
            object_depth = round(object_depth, 2)

            # 绘制边界框和深度
            cv2.rectangle(frame_left, (x1, y1), (x2, y2), (0, 255, 0), 2)
            cv2.putText(frame_left, f"{label} {object_depth}m", (x1, y1 - 10),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0), 2)

        # 显示结果
        cv2.imshow("Left Camera - Object Detection", frame_left)

        # 归一化视差图以显示深度信息
        # disparity_normalized = cv2.normalize(disparity, None, alpha=0, beta=255, norm_type=cv2.NORM_MINMAX)
        # disparity_normalized = np.uint8(disparity_normalized)
        # cv2.imshow("Disparity Map - Depth Information", disparity_normalized)

        # 按下 'q' 键退出
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    # 释放摄像头资源
    cap_left.release()
    cap_right.release()
    cv2.destroyAllWindows()

if __name__ == "__main__":
    main()