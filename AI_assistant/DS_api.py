from flask import Flask, Response, request
from openai import OpenAI
import json
from werkzeug.middleware.proxy_fix import ProxyFix  # 用于代理环境下正确处理HTTPS

app = Flask(__name__)

# 强制所有请求使用HTTPS（生产环境必备）
app.wsgi_app = ProxyFix(app.wsgi_app, x_proto=1, x_host=1)

# 安全配置（生产环境必须！）
app.config.update(
    SESSION_COOKIE_SECURE=True,
    SESSION_COOKIE_HTTPONLY=True,
    SESSION_COOKIE_SAMESITE='Lax',
    PREFERRED_URL_SCHEME='https'
)

# 从环境变量读取API Key（推荐）
# import os
# client = OpenAI(api_key=os.getenv("DEEPSEEK_API_KEY"), base_url="https://api.deepseek.com")
#⚠️⚠️⚠️此项目涉及经费，请按需求使用apikey⚠️⚠️⚠️
client = OpenAI(api_key="此处替换您的apikey", base_url="https://api.deepseek.com")
#⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️
@app.route('/chat_stream', methods=['POST'])
def chat_stream():
    # 检查请求是否来自HTTPS（非调试模式下强制）
    if not request.is_secure and not app.debug:
        return {"error": "HTTPS required"}, 403

    # 获取用户消息
    user_message = request.json.get('message', '')
    if not user_message:
        return {"error": "Message is required"}, 400

    def generate():
        response = client.chat.completions.create(
            model="deepseek-chat",
            messages=[
                {"role": "system", "content": "You are a helpful assistant"},
                {"role": "user", "content": user_message},
            ],
            stream=True
        )
        for chunk in response:
            if chunk.choices[0].delta.content:
                yield f"data: {json.dumps({'text': chunk.choices[0].delta.content})}\n\n"

    return Response(generate(), mimetype='text/event-stream')
#⚠️⚠️⚠️下方host需替换，请自行查询ip4地址⚠️⚠️⚠️
if __name__ == '__main__':
    # 本地测试用自签名证书（生产环境请替换为正规证书）
    context = ('ssl.cert', 'ssl.key')  # 需提前生成证书
    app.run(
        host='0.0.0.0',
        port=5004,
        ssl_context=context,  # 启用HTTPS
        debug=True
    )
