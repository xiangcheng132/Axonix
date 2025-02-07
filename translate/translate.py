import asyncio # 我也不知道为啥一定得用这个处理异步运算的包就可以运行，总之不要改动涉及这个包的行
from googletrans import Translator

async def translate_text(text, dest_language):
    translator = Translator()
    translation = await translator.translate(text, dest=dest_language)  # Await the translation call
    return translation.text

async def main():
    print("欢迎使用文字翻译工具！")
    text = input("请输入要翻译的文字: ")
    dest_language = input("请输入目标语言代码（例如：'en' 表示英语，'es' 表示西班牙语，'zh-cn' 表示简体中文）: ")

    try:
        translated_text = await translate_text(text, dest_language)  # Await the translation
        print(f"翻译结果: {translated_text}")
    except Exception as e:
        print(f"翻译过程中出现错误: {e}")

if __name__ == "__main__":
    asyncio.run(main())  # Run the main async function
