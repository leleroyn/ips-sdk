"""
IPS SDK Python - 响应模型
"""

from typing import Any, Generic, Optional, TypeVar

from pydantic import BaseModel, ConfigDict, Field


T = TypeVar("T")


class IdCardResult(BaseModel):
    """身份证检测结果"""
    model_config = ConfigDict(populate_by_name=True)
    
    corp_image_base64: Optional[str] = Field(None, alias="corp_image_base64")
    confidence: Optional[float] = None
    idcard_type: Optional[int] = Field(None, alias="idcard_type")
    ocr_text: Optional[str] = Field(None, alias="ocr_text")


class BizLicResult(BaseModel):
    """营业执照检测结果"""
    model_config = ConfigDict(populate_by_name=True)
    
    corp_image_base64: Optional[str] = Field(None, alias="corp_image_base64")
    confidence: Optional[float] = None
    bizlic_type: Optional[int] = Field(None, alias="bizlic_type")
    ocr_text: Optional[str] = Field(None, alias="ocr_text")


class SealResult(BaseModel):
    """印章检测结果"""
    model_config = ConfigDict(populate_by_name=True)
    
    seal_image_base64: Optional[str] = Field(None, alias="seal_image_base64")
    confidence: Optional[float] = None
    seal_type: Optional[int] = Field(None, alias="seal_type")
    ocr_result: Optional[str] = Field(None, alias="ocr_result")


class CardInfo(BaseModel):
    """证件信息"""
    model_config = ConfigDict(populate_by_name=True)
    
    card_type: Optional[int] = Field(None, alias="card_type")
    confidence: Optional[float] = None


class CardResult(BaseModel):
    """通用证件检测结果"""
    model_config = ConfigDict(populate_by_name=True)
    
    corp_image_base64: Optional[str] = Field(None, alias="corp_image_base64")
    card_info: Optional[CardInfo] = Field(None, alias="card_info")
    fork: Optional[bool] = None


class InvoiceResult(BaseModel):
    """发票识别结果"""
    model_config = ConfigDict(populate_by_name=True)
    
    corp_image_base64: Optional[str] = Field(None, alias="corp_image_base64")
    confidence: Optional[float] = None
    invoice_type: Optional[int] = Field(None, alias="invoice_type")
    ocr_text: Optional[str] = Field(None, alias="ocr_text")
    source_text: Optional[str] = Field(None, alias="source_text")


class OcrResult(BaseModel):
    """OCR识别结果"""
    model_config = ConfigDict(populate_by_name=True)
    
    ocr_text: Optional[str] = Field(None, alias="ocr_text")


class HandprintResult(BaseModel):
    """手印检测结果"""
    model_config = ConfigDict(populate_by_name=True)
    
    has_handprint: Optional[bool] = Field(None, alias="has_handprint")


class HealthStatus(BaseModel):
    """健康检查状态"""
    status: Optional[str] = None
    timestamp: Optional[int] = None


class ApiResponse(BaseModel, Generic[T]):
    """统一响应包装"""
    model_config = ConfigDict(populate_by_name=True)
    
    success: bool
    status_code: int
    data: Optional[T] = None
    error_message: Optional[str] = Field(None, alias="error_message")
    
    @classmethod
    def ok(cls, status_code: int, data: Any = None) -> "ApiResponse":
        """创建成功响应"""
        return cls(success=True, status_code=status_code, data=data)
    
    @classmethod
    def error(cls, status_code: int, error_message: str) -> "ApiResponse":
        """创建错误响应"""
        return cls(success=False, status_code=status_code, error_message=error_message)
    
    def is_success(self) -> bool:
        return self.success
