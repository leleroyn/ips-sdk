"""
IPS SDK Python - 请求模型
"""

from typing import Optional

from pydantic import BaseModel, Field


class ToolConfig(BaseModel):
    """工具配置"""
    init_confidence: Optional[float] = Field(None, alias="init_confidence")
    resize: Optional[bool] = Field(None, alias="resize")
    back_ground: Optional[bool] = Field(None, alias="back_ground")
    
    class Config:
        populate_by_name = True


class IdCardRequest(BaseModel):
    """身份证检测请求"""
    image_base64: str = Field(..., alias="image_base64")
    return_corp_image: Optional[bool] = Field(None, alias="return_corp_image")
    return_ocr_text: Optional[bool] = Field(None, alias="return_ocr_text")
    tool: Optional[ToolConfig] = None
    
    class Config:
        populate_by_name = True


class BizLicRequest(BaseModel):
    """营业执照检测请求"""
    image_base64: str = Field(..., alias="image_base64")
    return_corp_image: Optional[bool] = Field(None, alias="return_corp_image")
    return_ocr_text: Optional[bool] = Field(None, alias="return_ocr_text")
    tool: Optional[ToolConfig] = None
    
    class Config:
        populate_by_name = True


class SealRequest(BaseModel):
    """印章检测请求"""
    image_base64: str = Field(..., alias="image_base64")
    file_type: Optional[str] = Field(None, alias="file_type")
    return_seal_image: Optional[bool] = Field(None, alias="return_seal_image")
    return_ocr_text: Optional[bool] = Field(None, alias="return_ocr_text")
    tool: Optional[ToolConfig] = None
    
    class Config:
        populate_by_name = True


class CardRequest(BaseModel):
    """通用证件检测请求"""
    image_base64: str = Field(..., alias="image_base64")
    return_corp_image: Optional[bool] = Field(None, alias="return_corp_image")
    
    class Config:
        populate_by_name = True


class InvoiceRequest(BaseModel):
    """发票识别请求"""
    image_base64: str = Field(..., alias="image_base64")
    file_type: Optional[str] = Field(None, alias="file_type")
    return_corp_image: Optional[bool] = Field(None, alias="return_corp_image")
    return_ocr_text: Optional[bool] = Field(None, alias="return_ocr_text")
    tool: Optional[ToolConfig] = None
    
    class Config:
        populate_by_name = True


class OcrRequest(BaseModel):
    """OCR识别请求"""
    image_base64: str = Field(..., alias="image_base64")
    file_type: Optional[str] = Field(None, alias="file_type")
    return_image: Optional[bool] = Field(None, alias="return_image")
    return_text: Optional[bool] = Field(None, alias="return_text")
    
    class Config:
        populate_by_name = True


# ===== Upload Params =====

class IdCardUploadParams(BaseModel):
    """身份证上传参数 (v3)"""
    return_corp_image: bool = Field(True, alias="return_corp_image")
    return_ocr_text: bool = Field(False, alias="return_ocr_text")
    init_confidence: float = Field(0.5, alias="init_confidence")
    resize: bool = Field(True, alias="resize")
    back_ground: bool = Field(False, alias="back_ground")
    
    class Config:
        populate_by_name = True
    
    def to_dict(self) -> dict:
        return {
            "return_corp_image": self.return_corp_image,
            "return_ocr_text": self.return_ocr_text,
            "init_confidence": self.init_confidence,
            "resize": self.resize,
            "back_ground": self.back_ground,
        }


class BizLicUploadParams(BaseModel):
    """营业执照上传参数 (v3)"""
    return_corp_image: bool = Field(True, alias="return_corp_image")
    return_ocr_text: bool = Field(False, alias="return_ocr_text")
    init_confidence: float = Field(0.8, alias="init_confidence")
    resize: bool = Field(True, alias="resize")
    back_ground: bool = Field(False, alias="back_ground")
    
    class Config:
        populate_by_name = True
    
    def to_dict(self) -> dict:
        return {
            "return_corp_image": self.return_corp_image,
            "return_ocr_text": self.return_ocr_text,
            "init_confidence": self.init_confidence,
            "resize": self.resize,
            "back_ground": self.back_ground,
        }


class SealUploadParams(BaseModel):
    """印章上传参数 (v3)"""
    return_seal_image: bool = Field(True, alias="return_seal_image")
    return_ocr_text: bool = Field(False, alias="return_ocr_text")
    init_confidence: float = Field(0.5, alias="init_confidence")
    resize: bool = Field(True, alias="resize")
    back_ground: bool = Field(True, alias="back_ground")
    
    class Config:
        populate_by_name = True
    
    def to_dict(self) -> dict:
        return {
            "return_seal_image": self.return_seal_image,
            "return_ocr_text": self.return_ocr_text,
            "init_confidence": self.init_confidence,
            "resize": self.resize,
            "back_ground": self.back_ground,
        }


class InvoiceUploadParams(BaseModel):
    """发票上传参数 (v3)"""
    return_corp_image: bool = Field(True, alias="return_corp_image")
    return_ocr_text: bool = Field(False, alias="return_ocr_text")
    init_confidence: float = Field(0.5, alias="init_confidence")
    resize: bool = Field(True, alias="resize")
    back_ground: bool = Field(False, alias="back_ground")
    
    class Config:
        populate_by_name = True
    
    def to_dict(self) -> dict:
        return {
            "return_corp_image": self.return_corp_image,
            "return_ocr_text": self.return_ocr_text,
            "init_confidence": self.init_confidence,
            "resize": self.resize,
            "back_ground": self.back_ground,
        }


class OcrUploadParams(BaseModel):
    """OCR上传参数 (v3)"""
    app_id: Optional[str] = Field(None, alias="app_id")
    resize: bool = Field(True, alias="resize")
    use_cls: bool = Field(False, alias="use_cls")
    format: str = Field("txt", alias="format")
    
    class Config:
        populate_by_name = True
    
    def to_dict(self) -> dict:
        result = {
            "resize": self.resize,
            "use_cls": self.use_cls,
            "format": self.format,
        }
        if self.app_id is not None:
            result["app_id"] = self.app_id
        return result


class HandprintParams(BaseModel):
    """手印检测参数 (v2)"""
    position: Optional[str] = None
    
    def to_dict(self) -> dict:
        if self.position:
            return {"position": self.position}
        return {}
