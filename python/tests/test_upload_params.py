"""
IPS SDK - 上传参数测试
"""

import pytest
from ips_sdk.models import (
    BizLicUploadParams,
    HandprintParams,
    IdCardUploadParams,
    InvoiceUploadParams,
    OcrUploadParams,
    SealUploadParams,
)


class TestUploadParams:
    """上传参数测试"""
    
    def test_idcard_upload_params_defaults(self):
        """测试 IdCardUploadParams 默认值"""
        params = IdCardUploadParams()
        assert params.return_corp_image is True
        assert params.return_ocr_text is False
        assert params.init_confidence == 0.5
        assert params.resize is True
        assert params.back_ground is False
    
    def test_idcard_upload_params_to_dict(self):
        """测试 IdCardUploadParams 转换为字典"""
        params = IdCardUploadParams(
            return_corp_image=True,
            return_ocr_text=True,
            init_confidence=0.7
        )
        d = params.to_dict()
        assert d["return_corp_image"] is True
        assert d["return_ocr_text"] is True
        assert d["init_confidence"] == 0.7
    
    def test_bizlic_upload_params_defaults(self):
        """测试 BizLicUploadParams 默认值"""
        params = BizLicUploadParams()
        assert params.init_confidence == 0.8  # 与 IdCard 不同
    
    def test_seal_upload_params_defaults(self):
        """测试 SealUploadParams 默认值"""
        params = SealUploadParams()
        assert params.return_seal_image is True
        assert params.back_ground is True  # 与 IdCard 不同
        assert params.init_confidence == 0.5
    
    def test_seal_upload_params_to_dict(self):
        """测试 SealUploadParams 转换为字典"""
        params = SealUploadParams(return_seal_image=False, init_confidence=0.9)
        d = params.to_dict()
        assert d["return_seal_image"] is False
        assert d["init_confidence"] == 0.9
    
    def test_invoice_upload_params_defaults(self):
        """测试 InvoiceUploadParams 默认值"""
        params = InvoiceUploadParams()
        assert params.init_confidence == 0.5
        assert params.back_ground is False
    
    def test_ocr_upload_params_defaults(self):
        """测试 OcrUploadParams 默认值"""
        params = OcrUploadParams()
        assert params.resize is True
        assert params.use_cls is False
        assert params.format == "txt"
        assert params.app_id is None
    
    def test_ocr_upload_params_to_dict(self):
        """测试 OcrUploadParams 转换为字典"""
        params = OcrUploadParams(format="json", app_id="my-app")
        d = params.to_dict()
        assert d["format"] == "json"
        assert d["app_id"] == "my-app"
        assert "app_id" in d  # app_id 非空时应包含
    
    def test_ocr_upload_params_to_dict_no_app_id(self):
        """测试 OcrUploadParams 不包含空的 app_id"""
        params = OcrUploadParams()
        d = params.to_dict()
        assert "app_id" not in d
    
    def test_handprint_params_empty(self):
        """测试 HandprintParams 无参数"""
        params = HandprintParams()
        assert params.position is None
        assert params.to_dict() == {}
    
    def test_handprint_params_with_position(self):
        """测试 HandprintParams 带位置"""
        params = HandprintParams(position="left")
        d = params.to_dict()
        assert d == {"position": "left"}
