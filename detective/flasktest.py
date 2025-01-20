from flask import Flask

app = Flask(__name__)

class App:
    def __init__(self):
        self.video = 'video'
        self.photo = 'photo'

    def plus(self):
        return self.video + self.photo
    def minus(self):
        return self.video + self.photo +"3"

# 创建类实例
app_instance = App()

@app.route('/plus')
def plus():
    return app_instance.plus()
@app.route('/minus')
def minus():
    return app_instance.minus()

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001, debug=True)