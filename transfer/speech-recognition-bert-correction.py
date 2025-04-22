import os
import pyaudio
import threading
import keyboard
import json
from vosk import Model, KaldiRecognizer
from transformers import AutoTokenizer, AutoModelForMaskedLM
import torch

# 强制使用 GPU
os.environ["CUDA_VISIBLE_DEVICES"] = "0"
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

# 定义全局变量
stop_recognition = False

# 模型路径
base_model_path = r"D:\anxio\Axonix-2\transfer\model"
vosk_model_path = os.path.join(base_model_path, "vosk-model-cn-kaldi-multicn-0.15")
bert_model_path = os.path.join(base_model_path, "bert-base-chinese")

# 初始化 BERT 模型和分词器
try:
    bert_tokenizer = AutoTokenizer.from_pretrained(bert_model_path)
    from transformers import BertForMaskedLM
    bert_model = BertForMaskedLM.from_pretrained(bert_model_path, ignore_mismatched_sizes=True).to(device)
    bert_model.eval()
except Exception as e:
    print(f"❌ BERT 模型加载失败: {e}")
    exit(1)

def correct_with_bert(text):
    """使用 BERT 进行语义纠错"""
    inputs = bert_tokenizer(text, return_tensors="pt", padding=True, truncation=True, max_length=512).to(device)
    with torch.no_grad():
        outputs = bert_model(**inputs)
    corrected_text = bert_tokenizer.decode(inputs['input_ids'][0], skip_special_tokens=True)
    return corrected_text

def recognize_audio():
    global stop_recognition

    # 加载 Vosk 语音识别模型
    if not os.path.exists(vosk_model_path):
        print("❌ 语音模型未找到，请下载模型并解压到指定路径")
        return

    model = Model(vosk_model_path)
    recognizer = KaldiRecognizer(model, 16000)
    recognizer.SetWords(True)

    # 音频流配置
    audio = pyaudio.PyAudio()
    stream = audio.open(format=pyaudio.paInt16, channels=1, rate=16000, input=True, frames_per_buffer=4000)
    stream.start_stream()

    print("🎤 语音识别开始（按 's' 退出）...")

    full_text = ""
    sentence_list = []  # 用于存储每个短句

    try:
        while not stop_recognition:
            data = stream.read(4000, exception_on_overflow=False)
            if recognizer.AcceptWaveform(data):
                result_json = recognizer.Result()
                result_dict = json.loads(result_json)
                new_text = result_dict.get("text", "")
                # 移除短句内部的空格
                new_text = ''.join(new_text.split())
                full_text += " " + new_text
                sentence_list.append(new_text)  # 将新的短句添加到列表
                print(f"\r✅ 识别结果: {' '.join(sentence_list)}", end="")
            else:
                partial_result_json = recognizer.PartialResult()
                partial_result_dict = json.loads(partial_result_json)
                partial_text = partial_result_dict.get("partial", "")
                if partial_text:
                    print(f"\r🔄 临时结果: {partial_text}", end="")
    except Exception as e:
        print(f"\n❗ 语音识别出错: {e}")
    finally:
        stream.stop_stream()
        stream.close()
        audio.terminate()
        # 输出最终优化结果
        if full_text:
            final_corrected_text = correct_with_bert(full_text)
            # 移除多余的空格
            final_corrected_text = ''.join(final_corrected_text.split())
            print(f"\n🏁 最终优化结果:\n{' '.join(sentence_list)}")
        print("🔴 语音识别结束。")

def listen_for_stop():
    global stop_recognition
    while not stop_recognition:
        if keyboard.is_pressed('s'):
            print("\n🛑 停止命令收到，正在退出...")
            stop_recognition = True
            break

if __name__ == "__main__":
    # 启动按键监听线程
    stop_thread = threading.Thread(target=listen_for_stop, daemon=True)
    stop_thread.start()

    # 启动语音识别
    recognize_audio()