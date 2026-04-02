"""
IPS SDK - 模型测试
"""

import pytest
from ips_sdk.models import (
    ApiResponse,
    BizLicResult,
    CardInfo,
    CardResult,
    IdCardRequest,
    IdCardResult,
    InvoiceResult,
    OcrResult,
    SealResult,
)


class TestModels:
    """模型测试"""
    
    def test_idcard_request_creation(self):
        """测试 IdCardRequest 创建"""
        req = IdCardRequest(image_base64="base64_data")
        assert req.image_base64 == "base64_data"
        assert req.return_corp_image is None
        assert req.return_ocr_text is None
    
    def test_idcard_request_with_params(self):
        """测试带参数的 IdCardRequest"""
        req = IdCardRequest(
            image_base64="base64_data",
            return_corp_image=True,
            return_ocr_text=True
        )
        assert req.return_corp_image is True
        assert req.return_ocr_text is True
    
    def test_idcard_result_parsing(self):
        """测试 IdCardResult 解析"""
        data = {
            "corp_image_base64": "img_base64_data",
            "confidence": 0.95,
            "idcard_type": 0,
            "ocr_text": "张三\n身份证号: xxx"
        }
        result = IdCardResult(**data)
        assert result.confidence == 0.95
        assert result.idcard_type == 0
        assert "张三" in result.ocr_text
    
    def test_idcard_result_nullable_fields(self):
        """测试 IdCardResult 可空字段"""
        result = IdCardResult(confidence=0.8)
        assert result.corp_image_base64 is None
        assert result.ocr_text is None
    
    def test_bizlic_result_parsing(self):
        """测试 BizLicResult 解析"""
        data = {
            "corp_image_base64": "img",
            "confidence": 0.9,
            "bizlic_type": 1,
            "ocr_text": "公司名称"
        }
        result = BizLicResult(**data)
        assert result.bizlic_type == 1
        assert "公司名称" in result.ocr_text
    
    def test_seal_result_parsing(self):
        """测试 SealResult 解析"""
        data = {
            "seal_image_base64": "seal_img",
            "confidence": 0.85,
            "seal_type": 2,
            "ocr_result": "印章内容"
        }
        result = SealResult(**data)
        assert result.seal_type == 2
        assert result.seal_image_base64 == "seal_img"
        assert result.ocr_result == "印章内容"
    
    def test_invoice_result_parsing(self):
        """测试 InvoiceResult 解析"""
        data = {
            "corp_image_base64": "invoice_img",
            "confidence": 0.88,
            "invoice_type": 1,
            "ocr_text": "发票内容",
            "source_text": "原始文本"
        }
        result = InvoiceResult(**data)
        assert result.invoice_type == 1
        assert result.source_text == "原始文本"
    
    def test_ocr_result_parsing(self):
        """测试 OcrResult 解析"""
        result = OcrResult(ocr_text="识别的文字内容")
        assert result.ocr_text == "识别的文字内容"
    
    def test_ocr_result_nullable(self):
        """测试 OcrResult 可空字段"""
        result = OcrResult()
        assert result.ocr_text is None
    
    def test_card_info_parsing(self):
        """测试 CardInfo 解析"""
        data = {"card_type": 1, "confidence": 0.92}
        info = CardInfo(**data)
        assert info.card_type == 1
        assert info.confidence == 0.92
    
    def test_card_result_parsing(self):
        """测试 CardResult 解析"""
        data = {
            "corp_image_base64": "card_img",
            "card_info": {"card_type": 1, "confidence": 0.9},
            "fork": False
        }
        result = CardResult(**data)
        assert result.fork is False
        assert result.card_info.card_type == 1
    
    def test_api_response_success(self):
        """测试 ApiResponse 成功响应"""
        resp = ApiResponse(success=True, status_code=200, data={"key": "value"})
        assert resp.is_success() is True
        assert resp.error_message is None
        assert resp.data == {"key": "value"}
    
    def test_api_response_failure(self):
        """测试 ApiResponse 失败响应"""
        resp = ApiResponse(success=False, status_code=500, error_message="Server Error")
        assert resp.is_success() is False
        assert resp.error_message == "Server Error"
        assert resp.data is None
    
    def test_api_response_ok(self):
        """测试 ApiResponse.ok() 工厂方法"""
        resp = ApiResponse.ok(status_code=200, data=[1, 2, 3])
        assert resp.is_success() is True
        assert resp.data == [1, 2, 3]
    
    def test_api_response_error(self):
        """测试 ApiResponse.error() 工厂方法"""
        resp = ApiResponse.error(status_code=400, error_message="Bad Request")
        assert resp.is_success() is False
        assert resp.error_message == "Bad Request"
