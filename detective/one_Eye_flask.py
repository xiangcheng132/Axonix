import cv2
import tkinter as tk
from tkinter import Frame, Label, Button
from PIL import Image, ImageTk
from collections import deque
import numpy as np
from ultralytics import YOLO
from flask import Flask, jsonify
import threading

# Initialize Flask app
app_flask = Flask(__name__)

class App(Frame):
    def __init__(self, root):
        super().__init__(root)
        self.root = root
        self.root.title("Object Detection and Distance Estimation")
        self.root.geometry("1600x1200")

        self.video_frame = Frame(root, width=1280, height=720, bg="black")
        self.video_frame.pack(pady=10)
        self.video_frame.pack_propagate(0)

        self.video_label = Label(self.video_frame)
        self.video_label.pack()

        self.start_button = Button(root, text="Start Detection", command=self.start_detection)
        self.start_button.pack(side=tk.LEFT, padx=10, pady=10)

        self.stop_button = Button(root, text="Stop Detection", command=self.stop_detection)
        self.stop_button.pack(side=tk.RIGHT, padx=10, pady=10)

        self.cap = cv2.VideoCapture(0)
        self.model = YOLO('../yolov8n.pt')
        self.KNOWN_WIDTH = 20.0
        self.FOCAL_LENGTH = 700

        self.running = False
        self.distance_deque = deque(maxlen=10)
        self.conf_deque = deque(maxlen=10)

    def calculate_distance(self, known_width, focal_length, perceived_width):
        return (known_width * focal_length) / perceived_width

    def smooth_distance(self, distance, confidence):
        self.distance_deque.append(distance)
        self.conf_deque.append(confidence)
        weighted_distances = [d * c for d, c in zip(self.distance_deque, self.conf_deque)]
        total_conf = sum(self.conf_deque)
        return sum(weighted_distances) / total_conf if total_conf else 0

    def start_detection(self):
        self.running = True
        self.detect_objects()

    def stop_detection(self):
        self.running = False

    def detect_objects(self):
        if self.running and self.cap.isOpened():
            ret, frame = self.cap.read()
            if ret:
                frame = cv2.resize(frame, (1280, 720))
                results = self.model(frame)
                detections = results[0].boxes.data.cpu().numpy()

                for detection in detections:
                    x1, y1, x2, y2, confidence, class_id = detection[:6]
                    label = self.model.names[int(class_id)]
                    if confidence > 0.4:
                        perceived_width = x2 - x1
                        distance = self.calculate_distance(self.KNOWN_WIDTH, self.FOCAL_LENGTH, perceived_width)
                        smoothed_distance = self.smooth_distance(distance, confidence)
                        cv2.rectangle(frame, (int(x1), int(y1)), (int(x2), int(y2)), (0, 255, 0), 2)
                        text = f"{label}: {smoothed_distance:.2f} cm"
                        cv2.putText(frame, text, (int(x1), int(y1) - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 2)

                frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
                img = Image.fromarray(frame_rgb)
                imgtk = ImageTk.PhotoImage(image=img)
                self.video_label.imgtk = imgtk
                self.video_label.configure(image=imgtk)

            self.root.after(10, self.detect_objects)

    def on_closing(self):
        self.stop_detection()
        self.cap.release()
        cv2.destroyAllWindows()
        self.root.destroy()

# Flask routes for API
@app_flask.route('/start', methods=['POST'])
def start_detection_api():
    app.running = True
    app.detect_objects()
    return jsonify({"status": "Detection started"}), 200

@app_flask.route('/stop', methods=['POST'])
def stop_detection_api():
    app.running = False
    return jsonify({"status": "Detection stopped"}), 200

@app_flask.route('/status', methods=['GET'])
def get_status():
    return jsonify({"running": app.running}), 200

# Run Tkinter in a separate thread
def run_tkinter_app():
    root = tk.Tk()
    global app
    app = App(root)
    root.protocol("WM_DELETE_WINDOW", app.on_closing)
    root.mainloop()

if __name__ == "__main__":
    threading.Thread(target=run_tkinter_app).start()
    app_flask.run(port=5000)