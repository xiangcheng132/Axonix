import hashlib
import random
import logging
import os
from flask import Flask, request, abort
import requests

# 百度翻译API配置
#请自行前往百度API开发平台申请API替换下面的占位符❗❗❗❗❗❗❗❗❗❗
BAIDU_APP_ID = "⚠⚠⚠⚠⚠⚠"
BAIDU_API_KEY = "⚠⚠⚠⚠⚠⚠"
BAIDU_API_URL = "https://fanyi-api.baidu.com/api/trans/vip/translate"

# 支持的语言代码列表
SUPPORTED_LANGUAGES = {
    "zh": "中文", "en": "英语", "jp": "日语", "kor": "韩语",
    "fra": "法语", "ru": "俄语", "de": "德语", "es": "西班牙语",
    "ara": "阿拉伯语", "nl": "荷兰语", "it": "意大利语", "pt": "葡萄牙语",
    "el": "希腊语", "hu": "匈牙利语", "pl": "波兰语", "bul": "保加利亚语",
    "cs": "捷克语", "swe": "瑞典语", "th": "泰语", "vie": "越南语",
    "tr": "土耳其语", "rom": "罗马尼亚语", "yue": "粤语", "wyw": "文言文"
}

# 初始化Flask
app = Flask(__name__)

# 配置日志格式
logging.basicConfig(
    format="%(levelname)s - %(message)s",
    level=logging.DEBUG  # 改为DEBUG级别，方便调试
)
logger = logging.getLogger(__name__)


def validate_translation_request(content):
    """手动实现请求参数验证"""
    if not content:
        abort(400, description="缺少必要字段: content")

    if len(content) == 0 or len(content) > 2000:
        abort(400, description="文本长度需在1 - 2000字符之间")


def generate_signature(text: str, salt: str) -> str:
    """生成百度API要求的MD5签名"""
    sign_str = f"{BAIDU_APP_ID}{text}{salt}{BAIDU_API_KEY}"
    return hashlib.md5(sign_str.encode()).hexdigest()


def call_baidu_api(params: dict):
    """同步调用百度API"""
    try:
        response = requests.post(
            BAIDU_API_URL,
            data=params,
            headers={"Content-Type": "application/x-www-form-urlencoded"},
            timeout=10
        )
        response.raise_for_status()
        return response.json()
    except requests.exceptions.Timeout:
        abort(504, description="翻译服务响应超时")
    except requests.exceptions.RequestException as e:
        logger.error(f"API请求失败: {str(e)}")
        abort(502, description="翻译服务不可用")


@app.route('/translate', methods=['POST'])
def translate():
    """整合后的接口"""
    content = request.data.decode('utf-8')
    logger.debug(f"Received content: {content}")

    # 参数校验
    validate_translation_request(content)

    # 判断源语言并设置目标语言
    from_lang = "auto"
    to_lang = "en" if any(u'\u4e00' <= char <= u'\u9fff' for char in content) else "zh"

    logger.info(f"Translation request from: {from_lang}, to: {to_lang}")

    # 执行翻译
    salt = str(random.randint(10000, 99999))
    params = {
        "q": content,
        "from": from_lang,
        "to": to_lang,
        "appid": BAIDU_APP_ID,
        "salt": salt,
        "sign": generate_signature(content, salt)
    }

    try:
        result = call_baidu_api(params)
        if error_code := result.get("error_code"):
            error_msg = result.get("error_msg", "未知错误")
            logger.error(f"API错误 {error_code}: {error_msg}")
            abort(500, description=f"翻译服务错误 [{error_code}]: {error_msg}")

        if not (trans_result := result.get("trans_result")):
            logger.error("无效的API响应结构")
            abort(500, description="翻译服务返回无效响应")

        translated_text = trans_result[0]["dst"]
        return translated_text
    except Exception as e:
        logger.exception("未处理的翻译异常")
        abort(500, description="内部服务器错误")


@app.errorhandler(400)
@app.errorhandler(500)
def handle_errors(error):
    """统一错误处理"""
    return f"{error.description}\n{error.code}"


if __name__ == "__main__":
    # 服务器地址
    #根据实际的IP地址修改❗❗❗❗❗❗❗❗❗❗❗❗❗❗❗
    SERVER_HOST = '0.0.0.0'
    SERVER_PORT = 5003

    # 清理旧证书
    if os.path.exists("key.pem"):
        os.remove("key.pem")
    if os.path.exists("cert.pem"):
        os.remove("cert.pem")

    # 生成新证书
    subj_param = f'/CN={SERVER_HOST}'
    os.system(f'openssl req -x509 -newkey rsa:4096 \
        -keyout key.pem -out cert.pem \
        -days 365 -nodes \
        -subj "{subj_param}"')

    # 启动服务器
    app.run(
        host=SERVER_HOST,
        port=SERVER_PORT,
        ssl_context=('cert.pem', 'key.pem'),
        debug=False
    )
    