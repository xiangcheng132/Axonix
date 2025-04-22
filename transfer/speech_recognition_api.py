import os
import pyaudio
import threading
import keyboard
import json
from vosk import Model, KaldiRecognizer
from transformers import AutoTokenizer, AutoModelForMaskedLM
import torch
from flask import Flask, request, jsonify

# 强制使用 GPU
os.environ["CUDA_VISIBLE_DEVICES"] = "0"
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

# 定义全局变量
stop_recognition = False
app = Flask(__name__)

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

# 全局变量
recognizer = None
stream = None
audio = None
sentence_list = []
full_text = ""
stop_recognition = False

def setup_vosk():
    """设置 Vosk 语音识别模型"""
    global recognizer, audio, stream
    if not os.path.exists(vosk_model_path):
        return "❌ 语音模型未找到，请下载模型并解压到指定路径"
    model = Model(vosk_model_path)
    recognizer = KaldiRecognizer(model, 16000)
    recognizer.SetWords(True)
    audio = pyaudio.PyAudio()
    stream = audio.open(format=pyaudio.paInt16, channels=1, rate=16000, input=True, frames_per_buffer=4000)
    stream.start_stream()
    return "✅ Vosk 模型加载成功"

@app.route('/start_recognition', methods=['POST'])
def start_recognition():
    """开始语音识别"""
    global stop_recognition, sentence_list, full_text
    setup_vosk()
    stop_recognition = False
    sentence_list = []
    full_text = ""
    threading.Thread(target=recognize_audio).start()
    return jsonify({"message": "语音识别已启动"})

@app.route('/stop_recognition', methods=['POST'])
def stop_recognition_api():
    """停止语音识别"""
    global stop_recognition
    stop_recognition = True
    return jsonify({
        "message": "语音识别已停止", 
        "final_result": ' '.join(sentence_list)
    })

def recognize_audio():
    """语音识别主函数"""
    global full_text, sentence_list, stop_recognition
    try:
        while not stop_recognition:
            data = stream.read(4000, exception_on_overflow=False)
            if recognizer.AcceptWaveform(data):
                result_json = recognizer.Result()
                result_dict = json.loads(result_json)
                new_text = result_dict.get("text", "")
                new_text = ''.join(new_text.split())
                full_text += " " + new_text
                sentence_list.append(new_text)
    except Exception as e:
        print(f"❗ 语音识别出错: {e}")
    finally:
        stream.stop_stream()
        stream.close()
        audio.terminate()

if __name__ == "__main__":
    # 生成自签名证书（仅用于测试，生产环境需替换为 CA 证书）
    os.system("openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -nodes -subj '/CN=localhost'")
    
    # 启动 HTTPS 服务器
    app.run(
        host='0.0.0.0',
        port=8000,
        ssl_context=('cert.pem', 'key.pem'),  # 配置 SSL 证书
        threaded=True
    )