"""
IPS SDK Python - SDK 入口
"""

from .client import IpsClient
from .config import IpsConfig
from .exceptions import IpsApiException, IpsNetworkException, IpsSdkException
from .http import FileInput
from .models import (
    ApiResponse,
    BizLicRequest,
    BizLicResult,
    BizLicUploadParams,
    CardRequest,
    CardResult,
    HandprintParams,
    HandprintResult,
    HealthStatus,
    IdCardRequest,
    IdCardResult,
    IdCardUploadParams,
    InvoiceRequest,
    InvoiceResult,
    InvoiceUploadParams,
    OcrRequest,
    OcrResult,
    OcrUploadParams,
    SealRequest,
    SealResult,
    SealUploadParams,
    ToolConfig,
)

__version__ = "1.0.0"

__all__ = [
    "IpsClient",
    "IpsConfig",
    "IpsApiException",
    "IpsNetworkException",
    "IpsSdkException",
    "FileInput",
    # Models
    "ApiResponse",
    "ToolConfig",
    "IdCardRequest",
    "IdCardResult",
    "IdCardUploadParams",
    "BizLicRequest",
    "BizLicResult",
    "BizLicUploadParams",
    "SealRequest",
    "SealResult",
    "SealUploadParams",
    "CardRequest",
    "CardResult",
    "InvoiceRequest",
    "InvoiceResult",
    "InvoiceUploadParams",
    "OcrRequest",
    "OcrResult",
    "OcrUploadParams",
    "HandprintParams",
    "HandprintResult",
    "HealthStatus",
]
