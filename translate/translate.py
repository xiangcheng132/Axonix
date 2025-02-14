import asyncio
from fastapi import FastAPI, HTTPException
from googletrans import Translator
from pydantic import BaseModel

app = FastAPI()

class TranslationRequest(BaseModel):
    text: str
    dest_language: str

async def translate_text(text, dest_language):
    translator = Translator()
    # 使用 asyncio.to_thread 将同步代码放到线程池中运行
    translation = await asyncio.to_thread(translator.translate, text, dest=dest_language)
    return translation.text

@app.post("/translate/")
async def translate(request: TranslationRequest):
    try:
        translated_text = await translate_text(request.text, request.dest_language)
        return {"translated_text": translated_text}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)

#调用需先进入translate目录
#cd translate
#uvicorn translaor:app --reload

