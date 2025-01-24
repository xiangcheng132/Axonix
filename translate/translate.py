import requests
#导入网络请求模块
import requests as requests

while True:
    sentence = input('请输入你要翻译的句子: ')#等待用户输入待翻译的内容
    API = 'http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i='
    url = 'http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i=早上好,同学们!'
    back = requests.get(f'{API}{sentence}').json()
    result = back['translateResult'][0][0]['tgt']
    print(result) #将翻译结果显示出来