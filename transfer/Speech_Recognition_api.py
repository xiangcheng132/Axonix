import os
import threading
import json
import wave
import logging
from vosk import Model, KaldiRecognizer
from flask import Flask, request, jsonify
from flask_cors import CORS
import string
from flask.json.provider import DefaultJSONProvider
import torch

# 自定义 JSON 编码器，不进行 ASCII 转义
class CustomJSONProvider(DefaultJSONProvider):
    ensure_ascii = False

# 配置日志
logging.basicConfig(level=logging.DEBUG, format='%(asctime)s - %(levelname)s - %(message)s')

app = Flask(__name__)
app.json = CustomJSONProvider(app)  # 使用自定义 JSON 编码器
app.config['JSON_AS_ASCII'] = False  # 解决中文乱码问题
CORS(app)
lock = threading.Lock()

# 强制使用 GPU
os.environ["CUDA_VISIBLE_DEVICES"] = "0"
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

# 模型路径（根据实际的存储地址修改）
base_model_path = r"D:\anxio\Axonix-2\transfer\model"
vosk_model_path = os.path.join(base_model_path, "vosk-model-cn-kaldi-multicn-0.15")

# 全局识别器（单例模式）
recognizer = None

# 设置置信度阈值
CONFIDENCE_THRESHOLD = 0.6

def init_vosk():
    """初始化 Vosk 识别器（线程安全）"""
    global recognizer
    with lock:
        if not recognizer:
            if not os.path.exists(vosk_model_path):
                raise FileNotFoundError(f"❌ Vosk 模型路径不存在: {vosk_model_path}")
            model = Model(vosk_model_path)
            recognizer = KaldiRecognizer(model, 16000)  # 设置正确的音频采样率
            recognizer.SetWords(True)
    return recognizer


@app.route('/aud', methods=['POST'])
def audio_stream():
    try:
        logging.info("收到 /aud 请求")
        logging.info(f"请求头: {request.headers}")
        logging.info(f"请求表单数据: {request.form}")
        logging.info(f"请求文件: {request.files}")

        init_vosk()
        # 检查是否有文件上传
        if 'audio' not in request.files:
            logging.error("未提供音频文件")
            return jsonify({"error": "未提供音频文件"}), 400
        file = request.files['audio']
        # 检查文件是否为空
        if file.filename == '':
            logging.error("空音频文件")
            return jsonify({"error": "空音频文件"}), 400
        logging.info(f"接收到的音频文件名为: {file.filename}")
        # 检查文件扩展名是否为.wav
        if not file.filename.lower().endswith('.wav'):
            logging.error("文件不是 WAV 格式")
            return jsonify({"error": "文件必须是 WAV 格式"}), 400

        # 保存传输过来的音频文件
        save_dir ='received_audio'
        if not os.path.exists(save_dir):
            os.makedirs(save_dir)
        save_path = os.path.join(save_dir, file.filename)
        file.seek(0)  # 确保文件指针在文件开头
        file.save(save_path)
        logging.info(f"音频文件已保存到: {save_path}")

        # Vosk 处理音频数据
        try:
            with lock:
                with wave.open(save_path, 'rb') as wav_file:
                    print(wav_file)  # 添加调试信息
                    # 初始化识别器状态
                    recognizer.Reset()
                    while True:
                        data = wav_file.readframes(4000)
                        if len(data) == 0:
                            break
                        if recognizer.AcceptWaveform(data):
                            # 部分识别结果
                            partial_result = recognizer.PartialResult()
                            logging.debug(f"部分识别结果: {partial_result}")
                    # 最终识别结果
                    final_result_json = recognizer.FinalResult()
                    final_result_dict = json.loads(final_result_json)
                    logging.debug(f"最终识别结果 JSON: {final_result_dict}")

                    # 过滤掉置信度较低的识别结果
                    filtered_result = [word for word in final_result_dict.get('result', []) if word['conf'] >= CONFIDENCE_THRESHOLD]
                    # 按照时间顺序排序
                    sorted_result = sorted(filtered_result, key=lambda x: x['start'])
                    # 添加调试信息，输出排序后的结果
                    logging.debug(f"排序后的识别结果: {sorted_result}")
                    # 按顺序提取词
                    final_text_list = []
                    for word in sorted_result:
                        final_text_list.append(word['word'])
                    final_text = "".join(final_text_list)  # 直接拼接成字符串
                    # 添加调试信息，输出最终文本
                    logging.info(f"Vosk 识别结果: {final_text}")
                    return final_text
        except Exception as e:
            logging.error(f"Vosk 识别出错: {e}")
            return jsonify({"error": "Vosk 识别失败"}), 500

    except Exception as e:
        logging.error(f"处理失败: {e}")
        return jsonify({"error": f"处理失败: {str(e)}"}), 500


if __name__ == "__main__":
    # 服务器配置
    #IP地址根据自己的实际地址修改
    SERVER_HOST = '0.0.0.0'
    SERVER_PORT = 5000
    SERVER_IP = '0.0.0.0'
    # 生成自签名证书（确保 OpenSSL 已安装并添加到 PATH）
    if not (os.path.exists("key.pem") and os.path.exists("cert.pem")):
        os.system(
            f"openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -nodes -subj '/CN={SERVER_IP}'")
    # 启动 HTTPS 服务器
    app.run(
        host=SERVER_HOST,
        port=SERVER_PORT,
        ssl_context=('cert.pem', 'key.pem'),
        threaded=True,
        debug=False
    )