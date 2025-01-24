import os
import pyaudio
import threading
import keyboard  # 用于监听键盘事件
from vosk import Model, KaldiRecognizer

# 定义一个全局变量用于控制程序是否运行
stop_recognition = False

def recognize_audio():
    global stop_recognition

    # 下载或加载 Vosk 模型（支持中文和英文）
    model_path = "vosk-model-cn-kaldi-multicn-0.15"
    if not os.path.exists(model_path):
        print("请先下载支持中文和英文的模型，并将其解压到 'model' 文件夹中。")
        print("例如下载：https://alphacephei.com/vosk/models")
        return

    model = Model(model_path)
    recognizer = KaldiRecognizer(model, 16000)
    recognizer.SetWords(True)

    # 音频流配置
    audio = pyaudio.PyAudio()
    stream = audio.open(format=pyaudio.paInt16, channels=1, rate=16000, input=True, frames_per_buffer=4000)
    stream.start_stream()

    print("开始语音识别（按 's' 停止）...")
    try:
        while not stop_recognition:
            data = stream.read(4000, exception_on_overflow=False)
            if recognizer.AcceptWaveform(data):
                result = recognizer.Result()
                print("识别结果:", result)
            else:
                partial_result = recognizer.PartialResult()
                print("临时结果:", partial_result)
    except Exception as e:
        print(f"错误: {e}")
    finally:
        stream.stop_stream()
        stream.close()
        audio.terminate()
        print("语音识别结束。")

def listen_for_stop():
    global stop_recognition
    # 监听键盘事件，按下 's' 时设置标志为 True
    keyboard.wait('s')  # 等待按下 's'
    stop_recognition = True  # 设置标志，通知程序停止

if __name__ == "__main__":
    # 在一个单独的线程中监听键盘事件
    stop_thread = threading.Thread(target=listen_for_stop)
    stop_thread.daemon = True  # 设置为守护线程，主程序结束时自动退出
    stop_thread.start()

    # 主线程运行语音识别
    recognize_audio()