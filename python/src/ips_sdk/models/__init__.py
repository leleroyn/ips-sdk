"""
IPS SDK Python - 模型定义
"""

from .request import (
    ToolConfig,
    IdCardRequest,
    BizLicRequest,
    SealRequest,
    CardRequest,
    InvoiceRequest,
    OcrRequest,
    IdCardUploadParams,
    BizLicUploadParams,
    SealUploadParams,
    InvoiceUploadParams,
    OcrUploadParams,
    HandprintParams,
)
from .response import (
    IdCardResult,
    BizLicResult,
    SealResult,
    CardInfo,
    CardResult,
    InvoiceResult,
    OcrResult,
    HandprintResult,
    HealthStatus,
    ApiResponse,
)

__all__ = [
    # Request
    "ToolConfig",
    "IdCardRequest",
    "BizLicRequest",
    "SealRequest",
    "CardRequest",
    "InvoiceRequest",
    "OcrRequest",
    "IdCardUploadParams",
    "BizLicUploadParams",
    "SealUploadParams",
    "InvoiceUploadParams",
    "OcrUploadParams",
    "HandprintParams",
    # Response
    "IdCardResult",
    "BizLicResult",
    "SealResult",
    "CardInfo",
    "CardResult",
    "InvoiceResult",
    "OcrResult",
    "HandprintResult",
    "HealthStatus",
    "ApiResponse",
]
