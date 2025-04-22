import asyncio
import hashlib
import random
import logging
from typing import Dict
from fastapi import FastAPI, HTTPException, status
from pydantic import BaseModel, Field, field_validator
import httpx

# 百度翻译API配置
BAIDU_APP_ID = "20250303002291147"
BAIDU_API_KEY = "VBf8j4kXxJwZjsbRRhki"
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

# 初始化FastAPI
app = FastAPI(title="Baidu Translation API Proxy")

# 配置日志格式
logging.basicConfig(
    format="%(levelname)s - %(message)s",
    level=logging.INFO
)
logger = logging.getLogger(__name__)


class TranslationRequest(BaseModel):
    content: str = Field(
        ..., min_length=1, max_length=2000,
        description="待翻译文本"
    )
    input_type: str = Field(
        default="text",
        description="输入类型，目前仅支持 text，可扩展 audio 等"
    )
    dest_language: str = Field(
        ..., description=f"目标语言代码（支持：{', '.join(SUPPORTED_LANGUAGES.keys())}）"
    )
    braille_output: bool = Field(
        default=False,
        description="是否转换为盲文输出，当前不启用，仅预留"
    )

    @field_validator("dest_language")
    @classmethod
    def validate_language(cls, v: str) -> str:
        if v not in SUPPORTED_LANGUAGES:
            supported = ", ".join(SUPPORTED_LANGUAGES.keys())
            raise ValueError(f"暂不支持该语言代码，当前支持：{supported}")
        return v


def generate_signature(text: str, salt: str) -> str:
    """生成百度API要求的MD5签名"""
    sign_str = f"{BAIDU_APP_ID}{text}{salt}{BAIDU_API_KEY}"
    return hashlib.md5(sign_str.encode()).hexdigest()


async def call_baidu_api(params: Dict) -> Dict:
    """封装百度API调用，实现重试机制"""
    async with httpx.AsyncClient(timeout=10.0) as client:
        for attempt in range(3):
            try:
                response = await client.post(
                    BAIDU_API_URL,
                    data=params,
                    headers={"Content-Type": "application/x-www-form-urlencoded"}
                )
                response.raise_for_status()
                return response.json()
            except httpx.ReadTimeout:
                if attempt == 2:
                    raise HTTPException(
                        status_code=status.HTTP_504_GATEWAY_TIMEOUT,
                        detail="翻译服务响应超时"
                    )
                await asyncio.sleep(0.5 * attempt)
            except httpx.HTTPError as e:
                logger.error(f"API请求失败: {str(e)}")
                raise HTTPException(
                    status_code=status.HTTP_502_BAD_GATEWAY,
                    detail="翻译服务不可用"
                )


async def baidu_translate(text: str, dest_lang: str) -> str:
    """执行翻译核心逻辑"""
    salt = str(random.randint(10000, 99999))
    params = {
        "q": text,
        "from": "auto",
        "to": dest_lang,
        "appid": BAIDU_APP_ID,
        "salt": salt,
        "sign": generate_signature(text, salt)
    }

    logger.info(f"Translating text: {text[:50]}... (length: {len(text)})")
    
    try:
        result = await call_baidu_api(params)
        if error_code := result.get("error_code"):
            error_msg = result.get("error_msg", "未知错误")
            logger.error(f"API错误 {error_code}: {error_msg}")
            raise HTTPException(
                status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
                detail=f"翻译服务错误 [{error_code}]: {error_msg}"
            )
        
        if not (trans_result := result.get("trans_result")):
            logger.error("无效的API响应结构")
            raise HTTPException(
                status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
                detail="翻译服务返回无效响应"
            )
            
        return trans_result[0]["dst"]
    except HTTPException:
        raise
    except Exception as e:
        logger.exception("未处理的翻译异常")
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail="翻译处理失败"
        )


@app.post("/translate/", 
          summary="文本翻译接口",
          description="使用百度翻译API进行文本翻译，支持自动检测源语言")
async def translate(request: TranslationRequest):
    """主翻译接口"""
    logger.info(f"Translation request to: {request.dest_language}, type={request.input_type}, braille={request.braille_output}")
    try:
        translated_text = await baidu_translate(request.content, request.dest_language)
        return {
            "translated_text": translated_text,
            "braille_output": request.braille_output  # 暂时未处理，仅回显
        }
    except HTTPException as he:
        logger.error(f"翻译失败: {he.detail}")
        raise he
    except Exception as e:
        logger.error(f"未捕获异常: {str(e)}")
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail="内部服务器错误"
        )


@app.get("/languages", 
         summary="支持语言列表",
         response_description="当前支持的目标语言代码")
async def get_supported_languages():
    """获取支持的语言列表"""
    return {
        "supported_languages": SUPPORTED_LANGUAGES,
        "notice": "源语言自动检测支持常用语言"
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(
        app,
        host="0.0.0.0",
        port=8000,
        ssl_keyfile="key.pem",    # SSL 私钥文件
        ssl_certfile="cert.pem",  # SSL 证书文件
        log_config=None
    )