"""
IPS SDK Python - 核心客户端
"""

from pathlib import Path
from typing import Any, Dict, List, Optional, Union

from .config import IpsConfig
from .exceptions import IpsApiException, IpsNetworkException, IpsSdkException
from .http import FileInput, HttpClient
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
)


class IpsClient:
    """IPS SDK 客户端"""
    
    def __init__(self, config: Optional[IpsConfig] = None):
        if config is None:
            config = IpsConfig()
        self._config = config
        self._http = HttpClient(
            base_url=config.base_url,
            timeout=config.timeout
        )
    
    def close(self) -> None:
        """关闭客户端"""
        self._http.close()
    
    def __enter__(self) -> "IpsClient":
        return self
    
    def __exit__(self, exc_type, exc_val, exc_tb) -> None:
        self.close()
    
    def _execute_request(
        self,
        parser: callable,
        request_supplier: callable
    ) -> ApiResponse:
        """执行请求并统一处理异常"""
        try:
            result = request_supplier()
            data = parser(result)
            return ApiResponse.ok(status_code=200, data=data)
        except IpsApiException as e:
            return ApiResponse.error(status_code=e.status_code, error_message=e.message)
        except IpsNetworkException as e:
            return ApiResponse.error(status_code=0, error_message=e.message)
        except IpsSdkException as e:
            return ApiResponse.error(status_code=-1, error_message=e.message)
        except Exception as e:
            return ApiResponse.error(status_code=-1, error_message=str(e))
    
    def _parse_list_response(self, data: Any, model_class: type) -> List:
        """解析列表响应"""
        if data is None:
            return []
        if isinstance(data, list):
            return [model_class(**item) for item in data]
        return []
    
    def _parse_single_response(self, data: Any, model_class: type):
        """解析单个响应"""
        if data is None:
            return None
        if isinstance(data, dict):
            return model_class(**data)
        return data
    
    def _normalize_file(self, file: Any, filename: Optional[str] = None) -> FileInput:
        """标准化文件输入"""
        if isinstance(file, FileInput):
            return file
        if filename is None:
            if isinstance(file, (str, Path)):
                filename = str(file)
            elif hasattr(file, "name"):
                filename = getattr(file, "name")
            else:
                filename = "file"
        return FileInput(file=file, filename=filename)
    
    # ===== v1 API (Base64) =====
    
    def idcard_preprocess(self, request: IdCardRequest) -> ApiResponse[List[IdCardResult]]:
        """身份证预处理 (v1 - Base64)"""
        return self._execute_request(
            lambda r: self._parse_list_response(r, IdCardResult),
            lambda: self._http.post_json("/api/v1/idcard/preprocess", request.model_dump(by_alias=True))
        )
    
    def bizlic_preprocess(self, request: BizLicRequest) -> ApiResponse[List[BizLicResult]]:
        """营业执照预处理 (v1 - Base64)"""
        return self._execute_request(
            lambda r: self._parse_list_response(r, BizLicResult),
            lambda: self._http.post_json("/api/v1/bizlic/preprocess", request.model_dump(by_alias=True))
        )
    
    def seal_preprocess(self, request: SealRequest) -> ApiResponse[List[SealResult]]:
        """印章预处理 (v1 - Base64)"""
        return self._execute_request(
            lambda r: self._parse_list_response(r, SealResult),
            lambda: self._http.post_json("/api/v1/seal/preprocess", request.model_dump(by_alias=True))
        )
    
    # ===== v2 API =====
    
    def card_detection(self, request: CardRequest) -> ApiResponse[CardResult]:
        """通用证件检测 (v2 - Base64)"""
        return self._execute_request(
            lambda r: self._parse_single_response(r, CardResult),
            lambda: self._http.post_json("/api/v2/card/det", request.model_dump(by_alias=True))
        )
    
    def ocr_preprocess_v2(self, request: OcrRequest) -> ApiResponse[List[OcrResult]]:
        """通用OCR识别 (v2 - Base64, 支持PDF)"""
        return self._execute_request(
            lambda r: self._parse_list_response(r, OcrResult),
            lambda: self._http.post_json("/api/v2/ocr/preprocess", request.model_dump(by_alias=True))
        )
    
    def seal_preprocess_v2(self, request: SealRequest) -> ApiResponse[List[SealResult]]:
        """印章识别 (v2 - Base64, 支持PDF)"""
        return self._execute_request(
            lambda r: self._parse_list_response(r, SealResult),
            lambda: self._http.post_json("/api/v2/seal/preprocess", request.model_dump(by_alias=True))
        )
    
    def invoice_preprocess_v2(self, request: InvoiceRequest) -> ApiResponse[List[InvoiceResult]]:
        """发票识别 (v2 - Base64, 支持PDF)"""
        return self._execute_request(
            lambda r: self._parse_list_response(r, InvoiceResult),
            lambda: self._http.post_json("/api/v2/invoice/preprocess", request.model_dump(by_alias=True))
        )
    
    def handprint_detect_v2(
        self,
        file: Any,
        filename: Optional[str] = None,
        position: Optional[str] = None
    ) -> ApiResponse[HandprintResult]:
        """手印检测 (v2 - 文件上传)"""
        file_input = self._normalize_file(file, filename)
        params = HandprintParams(position=position).to_dict()
        return self._execute_request(
            lambda r: self._parse_single_response(r, HandprintResult),
            lambda: self._http.upload_file("/api/v2/handprint/detect", file_input, params)
        )
    
    # ===== v3 API (文件上传) =====
    
    def idcard_preprocess_v3(
        self,
        file: Any,
        filename: Optional[str] = None,
        return_corp_image: bool = True,
        return_ocr_text: bool = False,
        init_confidence: float = 0.5,
        resize: bool = True,
        back_ground: bool = False
    ) -> ApiResponse[List[IdCardResult]]:
        """身份证预处理 (v3 - 文件上传)"""
        file_input = self._normalize_file(file, filename)
        params = IdCardUploadParams(
            return_corp_image=return_corp_image,
            return_ocr_text=return_ocr_text,
            init_confidence=init_confidence,
            resize=resize,
            back_ground=back_ground
        ).to_dict()
        return self._execute_request(
            lambda r: self._parse_list_response(r, IdCardResult),
            lambda: self._http.upload_file("/api/v3/idcard/preprocess", file_input, params)
        )
    
    def bizlic_preprocess_v3(
        self,
        file: Any,
        filename: Optional[str] = None,
        return_corp_image: bool = True,
        return_ocr_text: bool = False,
        init_confidence: float = 0.8,
        resize: bool = True,
        back_ground: bool = False
    ) -> ApiResponse[List[BizLicResult]]:
        """营业执照预处理 (v3 - 文件上传)"""
        file_input = self._normalize_file(file, filename)
        params = BizLicUploadParams(
            return_corp_image=return_corp_image,
            return_ocr_text=return_ocr_text,
            init_confidence=init_confidence,
            resize=resize,
            back_ground=back_ground
        ).to_dict()
        return self._execute_request(
            lambda r: self._parse_list_response(r, BizLicResult),
            lambda: self._http.upload_file("/api/v3/bizlic/preprocess", file_input, params)
        )
    
    def invoice_preprocess_v3(
        self,
        file: Any,
        filename: Optional[str] = None,
        return_corp_image: bool = True,
        return_ocr_text: bool = False,
        init_confidence: float = 0.5,
        resize: bool = True,
        back_ground: bool = False
    ) -> ApiResponse[List[InvoiceResult]]:
        """发票识别 (v3 - 文件上传)"""
        file_input = self._normalize_file(file, filename)
        params = InvoiceUploadParams(
            return_corp_image=return_corp_image,
            return_ocr_text=return_ocr_text,
            init_confidence=init_confidence,
            resize=resize,
            back_ground=back_ground
        ).to_dict()
        return self._execute_request(
            lambda r: self._parse_list_response(r, InvoiceResult),
            lambda: self._http.upload_file("/api/v3/invoice/preprocess", file_input, params)
        )
    
    def seal_preprocess_v3(
        self,
        file: Any,
        filename: Optional[str] = None,
        return_seal_image: bool = True,
        return_ocr_text: bool = False,
        init_confidence: float = 0.5,
        resize: bool = True,
        back_ground: bool = True
    ) -> ApiResponse[List[SealResult]]:
        """印章检测 (v3 - 文件上传)"""
        file_input = self._normalize_file(file, filename)
        params = SealUploadParams(
            return_seal_image=return_seal_image,
            return_ocr_text=return_ocr_text,
            init_confidence=init_confidence,
            resize=resize,
            back_ground=back_ground
        ).to_dict()
        return self._execute_request(
            lambda r: self._parse_list_response(r, SealResult),
            lambda: self._http.upload_file("/api/v3/seal/preprocess", file_input, params)
        )
    
    def card_detection_v3(
        self,
        file: Any,
        filename: Optional[str] = None,
        return_corp_image: bool = True
    ) -> ApiResponse[CardResult]:
        """通用证件检测 (v3 - 文件上传)"""
        file_input = self._normalize_file(file, filename)
        params = {"return_corp_image": return_corp_image}
        return self._execute_request(
            lambda r: self._parse_single_response(r, CardResult),
            lambda: self._http.upload_file("/api/v3/card/det", file_input, params)
        )
    
    def ocr_preprocess_v3(
        self,
        file: Any,
        filename: Optional[str] = None,
        app_id: Optional[str] = None,
        resize: bool = True,
        use_cls: bool = False,
        format: str = "txt"
    ) -> ApiResponse[OcrResult]:
        """OCR识别 (v3 - 文件上传)"""
        file_input = self._normalize_file(file, filename)
        params = OcrUploadParams(
            app_id=app_id,
            resize=resize,
            use_cls=use_cls,
            format=format
        ).to_dict()
        return self._execute_request(
            lambda r: self._parse_single_response(r, OcrResult),
            lambda: self._http.upload_file("/api/v3/ocr/preprocess", file_input, params)
        )
    
    # ===== 健康检查 =====
    
    def health_check(self) -> ApiResponse[HealthStatus]:
        """健康检查"""
        return self._execute_request(
            lambda r: self._parse_single_response(r, HealthStatus),
            lambda: self._http.get("/health")
        )
