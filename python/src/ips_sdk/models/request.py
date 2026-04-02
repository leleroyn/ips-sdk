"""
IPS SDK Python - 请求模型
"""

from typing import Any, Optional

from pydantic import BaseModel, Field, ConfigDict


class ToolConfig(BaseModel):
    """工具配置"""
    model_config = ConfigDict(populate_by_name=True)
    
    init_confidence: Optional[float] = Field(None, alias="init_confidence")
    resize: Optional[bool] = Field(None, alias="resize")
    back_ground: Optional[bool] = Field(None, alias="back_ground")


class IdCardRequest(BaseModel):
    """身份证检测请求"""
    model_config = ConfigDict(populate_by_name=True)
    
    image_base64: str = Field(..., alias="image_base64")
    return_corp_image: Optional[bool] = Field(None, alias="return_corp_image")
    return_ocr_text: Optional[bool] = Field(None, alias="return_ocr_text")
    tool: Optional[ToolConfig] = None


class BizLicRequest(BaseModel):
    """营业执照检测请求"""
    model_config = ConfigDict(populate_by_name=True)
    
    image_base64: str = Field(..., alias="image_base64")
    return_corp_image: Optional[bool] = Field(None, alias="return_corp_image")
    return_ocr_text: Optional[bool] = Field(None, alias="return_ocr_text")
    tool: Optional[ToolConfig] = None


class SealRequest(BaseModel):
    """印章检测请求"""
    model_config = ConfigDict(populate_by_name=True)
    
    image_base64: str = Field(..., alias="image_base64")
    file_type: Optional[str] = Field(None, alias="file_type")
    return_seal_image: Optional[bool] = Field(None, alias="return_seal_image")
    return_ocr_text: Optional[bool] = Field(None, alias="return_ocr_text")
    tool: Optional[ToolConfig] = None


class CardRequest(BaseModel):
    """通用证件检测请求"""
    model_config = ConfigDict(populate_by_name=True)
    
    image_base64: str = Field(..., alias="image_base64")
    return_corp_image: Optional[bool] = Field(None, alias="return_corp_image")


class InvoiceRequest(BaseModel):
    """发票识别请求"""
    model_config = ConfigDict(populate_by_name=True)
    
    image_base64: str = Field(..., alias="image_base64")
    file_type: Optional[str] = Field(None, alias="file_type")
    return_corp_image: Optional[bool] = Field(None, alias="return_corp_image")
    return_ocr_text: Optional[bool] = Field(None, alias="return_ocr_text")
    tool: Optional[ToolConfig] = None


class OcrRequest(BaseModel):
    """OCR识别请求"""
    model_config = ConfigDict(populate_by_name=True)
    
    image_base64: str = Field(..., alias="image_base64")
    file_type: Optional[str] = Field(None, alias="file_type")
    return_image: Optional[bool] = Field(None, alias="return_image")
    return_text: Optional[bool] = Field(None, alias="return_text")


# ===== Upload Params =====

class IdCardUploadParams(BaseModel):
    """身份证上传参数 (v3)"""
    model_config = ConfigDict(populate_by_name=True)
    
    return_corp_image: bool = True
    return_ocr_text: bool = False
    init_confidence: float = 0.5
    resize: bool = True
    back_ground: bool = False
    
    def to_dict(self) -> dict:
        return {
            "return_corp_image": self.return_corp_image,
            "return_ocr_text": self.return_ocr_text,
            "init_confidence": self.init_confidence,
            "resize": self.resize,
            "back_ground": self.back_ground,
        }
    
    @classmethod
    def builder(cls, **kwargs: Any) -> "IdCardUploadParams":
        """Builder 模式"""
        return cls(**kwargs)


class BizLicUploadParams(BaseModel):
    """营业执照上传参数 (v3)"""
    model_config = ConfigDict(populate_by_name=True)
    
    return_corp_image: bool = True
    return_ocr_text: bool = False
    init_confidence: float = 0.8
    resize: bool = True
    back_ground: bool = False
    
    def to_dict(self) -> dict:
        return {
            "return_corp_image": self.return_corp_image,
            "return_ocr_text": self.return_ocr_text,
            "init_confidence": self.init_confidence,
            "resize": self.resize,
            "back_ground": self.back_ground,
        }
    
    @classmethod
    def builder(cls, **kwargs: Any) -> "BizLicUploadParams":
        """Builder 模式"""
        return cls(**kwargs)


class SealUploadParams(BaseModel):
    """印章上传参数 (v3)"""
    model_config = ConfigDict(populate_by_name=True)
    
    return_seal_image: bool = True
    return_ocr_text: bool = False
    init_confidence: float = 0.5
    resize: bool = True
    back_ground: bool = True
    
    def to_dict(self) -> dict:
        return {
            "return_seal_image": self.return_seal_image,
            "return_ocr_text": self.return_ocr_text,
            "init_confidence": self.init_confidence,
            "resize": self.resize,
            "back_ground": self.back_ground,
        }
    
    @classmethod
    def builder(cls, **kwargs: Any) -> "SealUploadParams":
        """Builder 模式"""
        return cls(**kwargs)


class InvoiceUploadParams(BaseModel):
    """发票上传参数 (v3)"""
    model_config = ConfigDict(populate_by_name=True)
    
    return_corp_image: bool = True
    return_ocr_text: bool = False
    init_confidence: float = 0.5
    resize: bool = True
    back_ground: bool = False
    
    def to_dict(self) -> dict:
        return {
            "return_corp_image": self.return_corp_image,
            "return_ocr_text": self.return_ocr_text,
            "init_confidence": self.init_confidence,
            "resize": self.resize,
            "back_ground": self.back_ground,
        }
    
    @classmethod
    def builder(cls, **kwargs: Any) -> "InvoiceUploadParams":
        """Builder 模式"""
        return cls(**kwargs)


class OcrUploadParams(BaseModel):
    """OCR上传参数 (v3)"""
    model_config = ConfigDict(populate_by_name=True)
    
    app_id: Optional[str] = None
    resize: bool = True
    use_cls: bool = False
    format: str = "txt"
    
    def to_dict(self) -> dict:
        result = {
            "resize": self.resize,
            "use_cls": self.use_cls,
            "format": self.format,
        }
        if self.app_id is not None:
            result["app_id"] = self.app_id
        return result
    
    @classmethod
    def builder(cls, **kwargs: Any) -> "OcrUploadParams":
        """Builder 模式"""
        return cls(**kwargs)


class HandprintParams(BaseModel):
    """手印检测参数 (v2)"""
    position: Optional[str] = None
    
    def to_dict(self) -> dict:
        if self.position:
            return {"position": self.position}
        return {}
    
    @classmethod
    def builder(cls, **kwargs: Any) -> "HandprintParams":
        """Builder 模式"""
        return cls(**kwargs)
