from flask import Flask, Response, request, jsonify
from openai import OpenAI
import subprocess
import os
from werkzeug.middleware.proxy_fix import ProxyFix

app = Flask(__name__)
app.wsgi_app = ProxyFix(app.wsgi_app, x_proto=1, x_host=1)

# 安全配置
app.config.update(
    SESSION_COOKIE_SECURE=True,
    SESSION_COOKIE_HTTPONLY=True,
    SESSION_COOKIE_SAMESITE='Lax',
    PREFERRED_URL_SCHEME='https'
)

# 初始化OpenAI客户端（⚠️⚠️⚠️请妥善保管并使用自己的API Key⚠️⚠️⚠️）
client = OpenAI(api_key="⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️", base_url="https://api.deepseek.com")


def generate_self_signed_cert():
    """生成自签名证书"""
    if not os.path.exists("ssl.cert") or not os.path.exists("ssl.key"):
        try:
            subprocess.run([
                'openssl', 'req', '-x509', '-newkey', 'rsa:4096',
                '-keyout', 'ssl.key', '-out', 'ssl.cert',
                '-days', '365', '-nodes',
                '-subj', '/CN=172.20.10.3'#⚠️⚠️⚠️根据实际的IP地址修改⚠️⚠️⚠️
            ], check=True)
            print("✅ 自签名证书生成成功")
        except Exception as e:
            print(f"❌ 证书生成失败: {e}")
            exit(1)


@app.route('/chat_stream', methods=['POST'])
def chat_stream():
    if not request.is_secure and not app.debug:
        return jsonify({"error": "HTTPS required"}), 403

    try:
        # 直接获取请求体的字符串内容
        user_message = request.get_data(as_text=True)
        if not user_message:
            return jsonify({"error": "Message is required"}), 400
        print(f"接收到的内容: {user_message}")
    except Exception as e:
        return jsonify({"error": f"Invalid request data: {str(e)}"}), 400

    def generate():
        try:
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
                    output = chunk.choices[0].delta.content
                    print(f"返回的信息内容: {output}")
                    yield output
        except Exception as e:
            error_msg = str(e)
            print(f"返回的错误信息: {error_msg}")
            yield error_msg

    return Response(generate(), mimetype='text/plain')


if __name__ == '__main__':
    # 生成证书
    generate_self_signed_cert()

    # 启动服务器
    print(f"🚀 服务启动")
    app.run(
        host='0.0.0.0',
        port=5004,
        ssl_context=('ssl.cert', 'ssl.key'),
        threaded=True,
        debug=False
    )
    
