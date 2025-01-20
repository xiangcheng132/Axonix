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
        self.root.title("Object Detection and Distance Estimation")
        self.root.geometry("1600x1200")

        # 创建一个容器，用于限制视频显示区域
        self.video_frame = Frame(root, width=1280, height=720, bg="black")
        self.video_frame.pack(pady=10)
        self.video_frame.pack_propagate(0)  # 禁止自动调整大小

        # 创建视频显示的标签，并放入视频框架中
        self.video_label = Label(self.video_frame)
        self.video_label.pack()

        # 控制按钮
        self.start_button = Button(root, text="Start Detection", command=self.start_detection)
        self.start_button.pack(side=tk.LEFT, padx=10, pady=10)

        self.stop_button = Button(root, text="Stop Detection", command=self.stop_detection)
        self.stop_button.pack(side=tk.RIGHT, padx=10, pady=10)

        # 初始化摄像头和 YOLOv8 模型
        self.cap = cv2.VideoCapture(0)
        self.model = YOLO('yolov8x.pt')  # 使用 YOLOv8 模型，可以更换为 yolov8m.pt 或 yolov8l.pt 等
        self.KNOWN_WIDTH = 20.0  # 实际宽度（厘米）
        self.FOCAL_LENGTH = 700  # 校准获得的焦距（像素）

        self.running = False
        self.distance_deque = deque(maxlen=10)  # 保存最近10帧的距离测量值
        self.conf_deque = deque(maxlen=10)  # 保存最近10帧的置信度值

    # 距离测量函数
    def calculate_distance(self, known_width, focal_length, perceived_width):
        return (known_width * focal_length) / perceived_width

    # 平滑距离计算
    def smooth_distance(self, distance, confidence):
        self.distance_deque.append(distance)
        self.conf_deque.append(confidence)
        weighted_distances = [d * c for d, c in zip(self.distance_deque, self.conf_deque)]
        total_conf = sum(self.conf_deque)
        return sum(weighted_distances) / total_conf if total_conf else 0

    # 开始检测
    def start_detection(self):
        self.running = True
        self.detect_objects()

    # 停止检测
    def stop_detection(self):
        self.running = False

    # 物体识别和距离检测函数
    def detect_objects(self):
        if self.running and self.cap.isOpened():
            ret, frame = self.cap.read()
            if ret:
                frame = cv2.resize(frame, (1280, 720))  # 使用更高分辨率的帧

                # 使用 YOLOv8 模型检测物体
                results = self.model(frame)  # 直接将帧传递给 YOLOv8 模型
                detections = results[0].boxes.data.cpu().numpy()  # 获取检测结果

                for detection in detections:
                    x1, y1, x2, y2, confidence, class_id = detection[:6]
                    label = self.model.names[int(class_id)]

                    # 提高置信度阈值，减少远距离误检测
                    if confidence > 0.4:
                        perceived_width = x2 - x1
                        distance = self.calculate_distance(self.KNOWN_WIDTH, self.FOCAL_LENGTH, perceived_width)
                        smoothed_distance = self.smooth_distance(distance, confidence)

                        # 显示检测结果
                        cv2.rectangle(frame, (int(x1), int(y1)), (int(x2), int(y2)), (0, 255, 0), 2)
                        text = f"{label}: {smoothed_distance:.2f} cm"
                        cv2.putText(frame, text, (int(x1), int(y1) - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 0), 2)

                # 将帧转换为 Image 对象以显示在 Tkinter 标签上
                frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
                img = Image.fromarray(frame_rgb)
                imgtk = ImageTk.PhotoImage(image=img)
                self.video_label.imgtk = imgtk
                self.video_label.configure(image=imgtk)

            # 循环调用自身
            self.root.after(10, self.detect_objects)

    def on_closing(self):
        self.stop_detection()
        self.cap.release()
        cv2.destroyAllWindows()
        self.root.destroy()

# 创建并运行 Tkinter 窗口
root = tk.Tk()
app = App(root)
root.protocol("WM_DELETE_WINDOW", app.on_closing)
root.mainloop()