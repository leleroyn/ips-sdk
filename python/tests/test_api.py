"""
IPS SDK - 集成测试 (模拟)
"""

import io
from unittest.mock import MagicMock, patch

import pytest
from ips_sdk import IpsClient, IpsConfig
from ips_sdk.exceptions import IpsNetworkException
from ips_sdk.models import (
    BizLicRequest,
    IdCardRequest,
    IdCardResult,
    InvoiceRequest,
    OcrRequest,
    SealRequest,
)


class TestApiMocked:
    """API 模拟测试"""
    
    @pytest.fixture
    def mock_client(self):
        """创建带 mock 的客户端"""
        return IpsClient(IpsConfig(base_url="http://localhost:8000"))
    
    @pytest.fixture
    def sample_response(self):
        """样本 API 响应"""
        return {"status_code": 200, "data": []}
    
    def test_health_check_success(self, mock_client):
        """测试健康检查成功"""
        with patch.object(mock_client._http, 'get') as mock_get:
            mock_get.return_value = {"status": "healthy", "timestamp": 123456}
            
            response = mock_client.health_check()
            
            assert response.is_success()
            assert response.data.status == "healthy"
            mock_get.assert_called_once_with("/health")
    
    def test_health_check_failure(self, mock_client):
        """测试健康检查失败 - 网络错误"""
        with patch.object(mock_client._http, 'get') as mock_get:
            mock_get.side_effect = IpsNetworkException("Connection refused")
            
            response = mock_client.health_check()
            
            assert response.is_success() is False
            assert response.status_code == 0
            assert "网络错误" in response.error_message
    
    def test_idcard_preprocess_v3_success(self, mock_client):
        """测试身份证检测 v3 成功"""
        with patch.object(mock_client._http, 'upload_file') as mock_upload:
            mock_upload.return_value = [
                {"confidence": 0.95, "idcard_type": 0, "ocr_text": "张三"}
            ]
            
            response = mock_client.idcard_preprocess_v3(
                file=b"fake_image_data",
                filename="test.jpg"
            )
            
            assert response.is_success()
            assert len(response.data) == 1
            assert response.data[0].confidence == 0.95
            assert response.data[0].idcard_type == 0
    
    def test_idcard_preprocess_v3_with_params(self, mock_client):
        """测试身份证检测 v3 带参数"""
        with patch.object(mock_client._http, 'upload_file') as mock_upload:
            mock_upload.return_value = [{"confidence": 0.8}]
            
            response = mock_client.idcard_preprocess_v3(
                file="test.jpg",
                return_ocr_text=True,
                init_confidence=0.7
            )
            
            assert response.is_success()
            mock_upload.assert_called_once()
            call_args = mock_upload.call_args
            assert "test.jpg" in str(call_args)
    
    def test_idcard_preprocess_v3_bytes_input(self, mock_client):
        """测试身份证检测 v3 bytes 输入"""
        with patch.object(mock_client._http, 'upload_file') as mock_upload:
            mock_upload.return_value = [{"confidence": 0.9}]
            
            response = mock_client.idcard_preprocess_v3(
                file=b"image_bytes",
                filename="idcard.jpg"
            )
            
            assert response.is_success()
    
    def test_idcard_preprocess_v3_io_input(self, mock_client):
        """测试身份证检测 v3 IO 输入"""
        with patch.object(mock_client._http, 'upload_file') as mock_upload:
            mock_upload.return_value = [{"confidence": 0.85}]
            
            bio = io.BytesIO(b"fake image data")
            response = mock_client.idcard_preprocess_v3(
                file=bio,
                filename="test.jpg"
            )
            
            assert response.is_success()
    
    def test_bizlic_preprocess_v3_success(self, mock_client):
        """测试营业执照检测 v3 成功"""
        with patch.object(mock_client._http, 'upload_file') as mock_upload:
            mock_upload.return_value = [
                {"confidence": 0.88, "bizlic_type": 1, "ocr_text": "公司名称"}
            ]
            
            response = mock_client.bizlic_preprocess_v3(
                file="bizlic.jpg",
                init_confidence=0.8
            )
            
            assert response.is_success()
            assert response.data[0].bizlic_type == 1
    
    def test_seal_preprocess_v3_success(self, mock_client):
        """测试印章检测 v3 成功"""
        with patch.object(mock_client._http, 'upload_file') as mock_upload:
            mock_upload.return_value = [
                {"confidence": 0.92, "seal_type": 2, "seal_image_base64": "img"}
            ]
            
            response = mock_client.seal_preprocess_v3(
                file="seal.jpg",
                return_seal_image=True
            )
            
            assert response.is_success()
            assert response.data[0].seal_image_base64 == "img"
    
    def test_card_detection_v3_success(self, mock_client):
        """测试通用证件检测 v3 成功"""
        with patch.object(mock_client._http, 'upload_file') as mock_upload:
            mock_upload.return_value = {
                "card_info": {"card_type": 1, "confidence": 0.9},
                "fork": False
            }
            
            response = mock_client.card_detection_v3(
                file="card.jpg",
                return_corp_image=True
            )
            
            assert response.is_success()
            assert response.data.card_info.card_type == 1
    
    def test_ocr_preprocess_v3_success(self, mock_client):
        """测试 OCR 识别 v3 成功"""
        with patch.object(mock_client._http, 'upload_file') as mock_upload:
            mock_upload.return_value = {"ocr_text": "识别的文字内容"}
            
            response = mock_client.ocr_preprocess_v3(
                file="doc.pdf",
                format="txt"
            )
            
            assert response.is_success()
            assert response.data.ocr_text == "识别的文字内容"
    
    def test_invoice_preprocess_v3_success(self, mock_client):
        """测试发票识别 v3 成功"""
        with patch.object(mock_client._http, 'upload_file') as mock_upload:
            mock_upload.return_value = [
                {
                    "confidence": 0.87,
                    "invoice_type": 1,
                    "ocr_text": "发票内容",
                    "source_text": "原始文本"
                }
            ]
            
            response = mock_client.invoice_preprocess_v3(file="invoice.pdf")
            
            assert response.is_success()
            assert response.data[0].invoice_type == 1
            assert response.data[0].source_text == "原始文本"
    
    def test_handprint_detect_v2_success(self, mock_client):
        """测试手印检测 v2 成功"""
        with patch.object(mock_client._http, 'upload_file') as mock_upload:
            mock_upload.return_value = {"has_handprint": True}
            
            response = mock_client.handprint_detect_v2(
                file="handprint.jpg",
                position="left"
            )
            
            assert response.is_success()
            assert response.data.has_handprint is True
    
    def test_handprint_detect_v2_no_position(self, mock_client):
        """测试手印检测 v2 无位置参数"""
        with patch.object(mock_client._http, 'upload_file') as mock_upload:
            mock_upload.return_value = {"has_handprint": False}
            
            response = mock_client.handprint_detect_v2(file="handprint.jpg")
            
            assert response.is_success()
            assert response.data.has_handprint is False
    
    def test_idcard_preprocess_base64_success(self, mock_client):
        """测试身份证检测 v1 Base64 成功"""
        with patch.object(mock_client._http, 'post_json') as mock_post:
            mock_post.return_value = [
                {"confidence": 0.95, "idcard_type": 0}
            ]
            
            request = IdCardRequest(image_base64="base64_data")
            response = mock_client.idcard_preprocess(request)
            
            assert response.is_success()
            assert len(response.data) == 1
            mock_post.assert_called_once()
    
    def test_seal_preprocess_v2_success(self, mock_client):
        """测试印章识别 v2 成功"""
        with patch.object(mock_client._http, 'post_json') as mock_post:
            mock_post.return_value = [
                {"confidence": 0.9, "seal_type": 1, "ocr_result": "章"}
            ]
            
            request = SealRequest(image_base64="base64", file_type="pdf")
            response = mock_client.seal_preprocess_v2(request)
            
            assert response.is_success()
            assert response.data[0].ocr_result == "章"
    
    def test_api_error_handling(self, mock_client):
        """测试 API 错误处理"""
        from ips_sdk.exceptions import IpsApiException
        
        with patch.object(mock_client._http, 'upload_file') as mock_upload:
            mock_upload.side_effect = IpsApiException(
                status_code=400,
                message="Invalid file format"
            )
            
            response = mock_client.idcard_preprocess_v3(file="test.jpg")
            
            assert response.is_success() is False
            assert response.status_code == 400
            assert "Invalid file format" in response.error_message
    
    def test_network_error_handling(self, mock_client):
        """测试网络错误处理"""
        with patch.object(mock_client._http, 'upload_file') as mock_upload:
            mock_upload.side_effect = IpsNetworkException("Connection timeout")
            
            response = mock_client.idcard_preprocess_v3(file="test.jpg")
            
            assert response.is_success() is False
            assert response.status_code == 0
            assert "Connection timeout" in response.error_message


class TestClientContextManager:
    """客户端上下文管理器测试"""
    
    def test_client_as_context_manager(self):
        """测试客户端作为上下文管理器"""
        with IpsClient() as client:
            assert client is not None
    
    def test_client_close(self):
        """测试客户端关闭"""
        client = IpsClient()
        client.close()
        # 验证可以重复关闭
        client.close()
