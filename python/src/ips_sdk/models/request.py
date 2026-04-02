"""
IPS SDK Python - 请求模型

使用 Pydantic v2 最佳实践：
- 使用 ConfigDict 替代 class Config
- 使用 Field(validation_alias=...) 指定输入字段名
- 使用 model_dump(by_alias=True) 序列化时输出正确的 JSON 字段名
"""

from typing import Any, Optional

from pydantic import BaseModel, Field, ConfigDict


class ToolConfig(BaseModel):
    """工具配置"""
    model_config = ConfigDict(populate_by_name=True)
    
    init_confidence: Optional[float] = Field(default=None, validation_alias="init_confidence")
    resize: Optional[bool] = Field(default=None, validation_alias="resize")
    back_ground: Optional[bool] = Field(default=None, validation_alias="back_ground")


class IdCardRequest(BaseModel):
    """身份证检测请求"""
    model_config = ConfigDict(populate_by_name=True)
    
    image_base64: str = Field(validation_alias="image_base64")
    return_corp_image: Optional[bool] = Field(default=None, validation_alias="return_corp_image")
    return_ocr_text: Optional[bool] = Field(default=None, validation_alias="return_ocr_text")
    tool: Optional[ToolConfig] = None


class BizLicRequest(BaseModel):
    """营业执照检测请求"""
    model_config = ConfigDict(populate_by_name=True)
    
    image_base64: str = Field(validation_alias="image_base64")
    return_corp_image: Optional[bool] = Field(default=None, validation_alias="return_corp_image")
    return_ocr_text: Optional[bool] = Field(default=None, validation_alias="return_ocr_text")
    tool: Optional[ToolConfig] = None


class SealRequest(BaseModel):
    """印章检测请求"""
    model_config = ConfigDict(populate_by_name=True)
    
    image_base64: str = Field(validation_alias="image_base64")
    file_type: Optional[str] = Field(default=None, validation_alias="file_type")
    return_seal_image: Optional[bool] = Field(default=None, validation_alias="return_seal_image")
    return_ocr_text: Optional[bool] = Field(default=None, validation_alias="return_ocr_text")
    tool: Optional[ToolConfig] = None


class CardRequest(BaseModel):
    """通用证件检测请求"""
    model_config = ConfigDict(populate_by_name=True)
    
    image_base64: str = Field(validation_alias="image_base64")
    return_corp_image: Optional[bool] = Field(default=None, validation_alias="return_corp_image")


class InvoiceRequest(BaseModel):
    """发票识别请求"""
    model_config = ConfigDict(populate_by_name=True)
    
    image_base64: str = Field(validation_alias="image_base64")
    file_type: Optional[str] = Field(default=None, validation_alias="file_type")
    return_corp_image: Optional[bool] = Field(default=None, validation_alias="return_corp_image")
    return_ocr_text: Optional[bool] = Field(default=None, validation_alias="return_ocr_text")
    tool: Optional[ToolConfig] = None


class OcrRequest(BaseModel):
    """OCR识别请求"""
    model_config = ConfigDict(populate_by_name=True)
    
    image_base64: str = Field(validation_alias="image_base64")
    file_type: Optional[str] = Field(default=None, validation_alias="file_type")
    return_image: Optional[bool] = Field(default=None, validation_alias="return_image")
    return_text: Optional[bool] = Field(default=None, validation_alias="return_text")


# ===== Upload Params =====

class IdCardUploadParams(BaseModel):
    """身份证上传参数 (v3)"""
    model_config = ConfigDict(populate_by_name=True)
    
    return_corp_image: bool = Field(default=True, validation_alias="return_corp_image")
    return_ocr_text: bool = Field(default=False, validation_alias="return_ocr_text")
    init_confidence: float = Field(default=0.5, validation_alias="init_confidence")
    resize: bool = Field(default=True, validation_alias="resize")
    back_ground: bool = Field(default=False, validation_alias="back_ground")
    
    def to_dict(self) -> dict:
        return self.model_dump(by_alias=True, exclude_none=True)
    
    @classmethod
    def builder(cls, **kwargs: Any) -> "IdCardUploadParams":
        """Builder 模式"""
        return cls(**kwargs)


class BizLicUploadParams(BaseModel):
    """营业执照上传参数 (v3)"""
    model_config = ConfigDict(populate_by_name=True)
    
    return_corp_image: bool = Field(default=True, validation_alias="return_corp_image")
    return_ocr_text: bool = Field(default=False, validation_alias="return_ocr_text")
    init_confidence: float = Field(default=0.8, validation_alias="init_confidence")
    resize: bool = Field(default=True, validation_alias="resize")
    back_ground: bool = Field(default=False, validation_alias="back_ground")
    
    def to_dict(self) -> dict:
        return self.model_dump(by_alias=True, exclude_none=True)
    
    @classmethod
    def builder(cls, **kwargs: Any) -> "BizLicUploadParams":
        """Builder 模式"""
        return cls(**kwargs)


class SealUploadParams(BaseModel):
    """印章上传参数 (v3)"""
    model_config = ConfigDict(populate_by_name=True)
    
    return_seal_image: bool = Field(default=True, validation_alias="return_seal_image")
    return_ocr_text: bool = Field(default=False, validation_alias="return_ocr_text")
    init_confidence: float = Field(default=0.5, validation_alias="init_confidence")
    resize: bool = Field(default=True, validation_alias="resize")
    back_ground: bool = Field(default=True, validation_alias="back_ground")
    
    def to_dict(self) -> dict:
        return self.model_dump(by_alias=True, exclude_none=True)
    
    @classmethod
    def builder(cls, **kwargs: Any) -> "SealUploadParams":
        """Builder 模式"""
        return cls(**kwargs)


class InvoiceUploadParams(BaseModel):
    """发票上传参数 (v3)"""
    model_config = ConfigDict(populate_by_name=True)
    
    return_corp_image: bool = Field(default=True, validation_alias="return_corp_image")
    return_ocr_text: bool = Field(default=False, validation_alias="return_ocr_text")
    init_confidence: float = Field(default=0.5, validation_alias="init_confidence")
    resize: bool = Field(default=True, validation_alias="resize")
    back_ground: bool = Field(default=False, validation_alias="back_ground")
    
    def to_dict(self) -> dict:
        return self.model_dump(by_alias=True, exclude_none=True)
    
    @classmethod
    def builder(cls, **kwargs: Any) -> "InvoiceUploadParams":
        """Builder 模式"""
        return cls(**kwargs)


class OcrUploadParams(BaseModel):
    """OCR上传参数 (v3)"""
    model_config = ConfigDict(populate_by_name=True)
    
    app_id: Optional[str] = Field(default=None, validation_alias="app_id")
    resize: bool = Field(default=True, validation_alias="resize")
    use_cls: bool = Field(default=False, validation_alias="use_cls")
    format: str = Field(default="txt", validation_alias="format")
    
    def to_dict(self) -> dict:
        return self.model_dump(by_alias=True, exclude_none=True)
    
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
