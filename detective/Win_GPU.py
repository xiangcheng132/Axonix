import cv2
import tkinter as tk
from tkinter import Frame, Label, Button
from PIL import Image, ImageTk
from collections import deque
import threading
import queue
import time
import torch
import numpy as np
from ultralytics import YOLO

class UltraFastDetector:
    def __init__(self):
        # 硬件加速初始化
        self.cap = cv2.VideoCapture(0, cv2.CAP_DSHOW)
        self.cap.set(cv2.CAP_PROP_FRAME_WIDTH, 1280)
        self.cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 720)
        self.cap.set(cv2.CAP_PROP_HW_ACCELERATION, cv2.VIDEO_ACCELERATION_ANY)
        self.cap.set(cv2.CAP_PROP_FPS, 60)  # 限制摄像头帧率
        
        # 模型加速配置
        self.device = 'cuda' if torch.cuda.is_available() else 'cpu'
        self.model = YOLO('yolov8n.pt').half().to(self.device)
        self.model.fuse()  # 融合模型层
        
        # 多线程队列系统
        self.frame_queue = queue.Queue(maxsize=2)  # 仅保留最新2帧
        self.result_queue = queue.Queue(maxsize=1)  # 仅保留最新结果
        
        # 动态性能调节参数
        self.target_fps = 60
        self.last_process_time = time.perf_counter()
        self.adaptive_skip = 1  # 初始跳帧数
        
        # 距离测量配置
        self.KNOWN_WIDTH = 20.0
        self.FOCAL_LENGTH = 700
        self.distance_buffer = deque(maxlen=15)
        
        # 状态控制
        self.running = False
        self.processing = False

    def start(self):
        self.running = True
        # 启动高性能采集线程
        threading.Thread(target=self._camera_thread, daemon=True).start()
        # 启动GPU加速处理线程
        threading.Thread(target=self._inference_thread, daemon=True).start()

    def _camera_thread(self):
        while self.running:
            start = time.perf_counter()
            ret, frame = self.cap.read()
            if not ret:
                continue
            
            # 动态队列管理（只保留最新帧）
            if self.frame_queue.full():
                try:
                    self.frame_queue.get_nowait()
                except queue.Empty:
                    pass
            self.frame_queue.put(frame)
            
            # 动态采集间隔控制
            elapsed = time.perf_counter() - start
            sleep_time = max(0, (1/self.target_fps) - elapsed)
            time.sleep(sleep_time)

    def _inference_thread(self):
        while self.running:
            if self.frame_queue.empty() or self.processing:
                time.sleep(0.001)
                continue
                
            self.processing = True
            try:
                # 动态跳帧策略
                for _ in range(self.adaptive_skip):
                    if not self.frame_queue.empty():
                        frame = self.frame_queue.get()
                
                # 超快预处理
                resized = cv2.resize(frame, (480, 360))  # 降分辨率到480x360
                tensor = torch.from_numpy(resized).to(self.device, half=True).permute(2,0,1).unsqueeze(0)/255
                
                # 模型推理（启用TRT加速）
                with torch.no_grad():
                    results = self.model(tensor, augment=False)[0]
                
                # 极速后处理
                boxes = results.boxes.xyxy.cpu().numpy()
                classes = results.boxes.cls.cpu().numpy()
                confs = results.boxes.conf.cpu().numpy()
                
                # 坐标映射优化
                scale_x = 1280 / 480
                scale_y = 720 / 360
                boxes[:, [0,2]] *= scale_x
                boxes[:, [1,3]] *= scale_y
                
                # 距离计算批处理
                distances = (self.KNOWN_WIDTH * self.FOCAL_LENGTH) / (boxes[:,2] - boxes[:,0])
                avg_distance = np.mean(distances) if len(distances) else 0
                self.distance_buffer.append(avg_distance)
                
                # 绘制优化（仅关键元素）
                display_frame = frame.copy()
                for box, cls_id in zip(boxes, classes):
                    x1,y1,x2,y2 = map(int, box)
                    cv2.rectangle(display_frame, (x1,y1), (x2,y2), (0,255,0), 1)
                
                # 更新显示帧
                if self.result_queue.empty():
                    self.result_queue.put(display_frame)
                    
                # 动态调整处理频率
                process_time = time.perf_counter() - self.last_process_time
                self.adaptive_skip = int(process_time * self.target_fps)
                self.adaptive_skip = max(0, min(self.adaptive_skip, 3))
                self.last_process_time = time.perf_counter()
                
            except Exception as e:
                print(f"处理错误: {str(e)}")
            finally:
                self.processing = False

class App(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("超高速物体检测系统")
        self.geometry("1280x820")
        
        # 视频显示区域
        self.video_label = Label(self, bg="black")
        self.video_label.pack(pady=10)
        
        # 控制面板
        control_frame = Frame(self)
        control_frame.pack(pady=10)
        Button(control_frame, text="启动", command=self.start).pack(side=tk.LEFT, padx=10)
        Button(control_frame, text="停止", command=self.stop).pack(side=tk.LEFT, padx=10)
        
        # 性能监控
        self.fps_label = Label(self, text="FPS: 0")
        self.fps_label.pack()
        
        # 系统初始化
        self.detector = UltraFastDetector()
        self.last_update = time.perf_counter()
        self.frame_count = 0

    def start(self):
        self.detector.start()
        self.update_display()

    def stop(self):
        self.detector.running = False

    def update_display(self):
        try:
            # 获取最新帧
            if not self.detector.result_queue.empty():
                frame = self.detector.result_queue.get()
                
                # 超快显示更新
                img = Image.fromarray(cv2.cvtColor(frame, cv2.COLOR_BGR2RGB))
                imgtk = ImageTk.PhotoImage(image=img)
                self.video_label.imgtk = imgtk
                self.video_label.configure(image=imgtk)
                
                # FPS计算
                self.frame_count += 1
                elapsed = time.perf_counter() - self.last_update
                if elapsed >= 1:
                    fps = self.frame_count / elapsed
                    self.fps_label.config(text=f"FPS: {fps:.1f}")
                    self.last_update = time.perf_counter()
                    self.frame_count = 0
                    
            # 极限刷新率（使用tkinter的定时器上限）
            self.after(1, self.update_display)
            
        except Exception as e:
            print(f"显示更新异常: {str(e)}")
            self.stop()

if __name__ == "__main__":
    app = App()
    app.protocol("WM_DELETE_WINDOW", app.stop)
    app.mainloop()