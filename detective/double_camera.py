import cv2
import tkinter as tk
from tkinter import Frame, Label, Button
from PIL import Image, ImageTk
from collections import deque
import numpy as np
from ultralytics import YOLO


class App(Frame):
    def __init__(self, root):
        super().__init__(root)
        self.root = root
        self.root.title("Stereo Vision Distance Estimation")
        self.root.geometry("1600x1200")

        # 创建一个容器，用于限制视频显示区域
        self.video_frame = Frame(root, width=1280, height=720, bg="black")
        self.video_frame.pack(pady=10)
        self.video_frame.pack_propagate(0)

        # 创建视频显示的标签，并放入视频框架中
        self.video_label = Label(self.video_frame)
        self.video_label.pack()

        # 控制按钮
        self.start_button = Button(root, text="Start Detection", command=self.start_detection)
        self.start_button.pack(side=tk.LEFT, padx=10, pady=10)
        self.stop_button = Button(root, text="Stop Detection", command=self.stop_detection)
        self.stop_button.pack(side=tk.RIGHT, padx=10, pady=10)

        # 初始化摄像头和 YOLOv8 模型
        self.cap_left = cv2.VideoCapture(0)  # 左摄像头
        self.cap_right = cv2.VideoCapture(0)  # 右摄像头
        self.model = YOLO('yolov8n.pt')  # 使用 YOLOv8 模型，可以更换为 yolov8m.pt 或 yolov8l.pt 等

        # 双目摄像头参数
        self.baseline = 6.0  # 两摄像头间的基线距离，单位：厘米
        self.focal_length = 700  # 焦距，单位：像素（校准获得）

        self.running = False

    # 开始检测
    def start_detection(self):
        self.running = True
        self.detect_objects()

    # 停止检测
    def stop_detection(self):
        self.running = False

    # 双目距离测量函数
    def calculate_stereo_distance(self, disparity):
        with np.errstate(divide='ignore'):  # 忽略除以零的错误
            distance = (self.focal_length * self.baseline) / (disparity + 1e-6)
        return distance

    # 物体识别和距离检测函数
    def detect_objects(self):
        if self.running and self.cap_left.isOpened() and self.cap_right.isOpened():
            ret_left, frame_left = self.cap_left.read()
            ret_right, frame_right = self.cap_right.read()

            if ret_left and ret_right:
                frame_left = cv2.resize(frame_left, (1280, 720))
                frame_right = cv2.resize(frame_right, (1280, 720))

                # 使用 YOLOv8 检测左图像中的物体
                results = self.model(frame_left)
                detections = results[0].boxes.data.cpu().numpy()

                # 计算视差图
                gray_left = cv2.cvtColor(frame_left, cv2.COLOR_BGR2GRAY)
                gray_right = cv2.cvtColor(frame_right, cv2.COLOR_BGR2GRAY)
                stereo = cv2.StereoSGBM_create(
                    minDisparity=0,
                    numDisparities=64,
                    blockSize=9,
                    P1=8 * 3 * 9 ** 2,
                    P2=32 * 3 * 9 ** 2,
                    disp12MaxDiff=1,
                    uniquenessRatio=10,
                    speckleWindowSize=100,
                    speckleRange=32
                )
                disparity_map = stereo.compute(gray_left, gray_right).astype(np.float32) / 16.0

                # 遍历检测结果
                for detection in detections:
                    x1, y1, x2, y2, confidence, class_id = detection[:6]
                    label = self.model.names[int(class_id)]

                    # 提高置信度阈值，减少误检测
                    if confidence > 0.4:
                        # 提取检测框内的视差
                        roi_disparity = disparity_map[int(y1):int(y2), int(x1):int(x2)]
                        valid_disparity = roi_disparity[roi_disparity > 0]  # 过滤无效视差

                        if valid_disparity.size > 0:
                            mean_disparity = np.mean(valid_disparity)
                            distance = self.calculate_stereo_distance(mean_disparity)

                            # 显示检测结果
                            cv2.rectangle(frame_left, (int(x1), int(y1)), (int(x2), int(y2)), (0, 255, 0), 2)
                            text = f"{label}: {distance:.2f} cm"
                            cv2.putText(frame_left, text, (int(x1), int(y1) - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 0), 2)

                # 将帧转换为 Image 对象以显示在 Tkinter 标签上
                frame_rgb = cv2.cvtColor(frame_left, cv2.COLOR_BGR2RGB)
                img = Image.fromarray(frame_rgb)
                imgtk = ImageTk.PhotoImage(image=img)
                self.video_label.imgtk = imgtk
                self.video_label.configure(image=imgtk)

            # 循环调用自身
            self.root.after(10, self.detect_objects)

    def on_closing(self):
        self.stop_detection()
        self.cap_left.release()
        self.cap_right.release()
        cv2.destroyAllWindows()
        self.root.destroy()

# 创建并运行 Tkinter 窗口
root = tk.Tk()
app = App(root)
root.protocol("WM_DELETE_WINDOW", app.on_closing)
root.mainloop()