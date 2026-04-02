"""
IPS SDK - 异常测试
"""

import pytest
from ips_sdk.exceptions import IpsApiException, IpsNetworkException, IpsSdkException


class TestExceptions:
    """异常测试"""
    
    def test_ips_sdk_exception(self):
        """测试基础异常"""
        exc = IpsSdkException("SDK Error")
        assert exc.message == "SDK Error"
        assert str(exc) == "SDK Error"
    
    def test_ips_api_exception(self):
        """测试 API 异常"""
        exc = IpsApiException(
            status_code=400,
            message="Bad Request",
            response_body='{"error": "invalid"}'
        )
        assert exc.status_code == 400
        assert exc.response_body == '{"error": "invalid"}'
        assert "400" in str(exc)
        assert "Bad Request" in str(exc)
    
    def test_ips_network_exception(self):
        """测试网络异常"""
        exc = IpsNetworkException("Connection refused")
        # message 包含前缀 (这是预期行为)
        assert "Connection refused" in exc.message
        assert "网络错误" in str(exc)
        assert "Connection refused" in str(exc)
    
    def test_exception_inheritance(self):
        """测试异常继承关系"""
        assert issubclass(IpsApiException, IpsSdkException)
        assert issubclass(IpsNetworkException, IpsSdkException)
        assert issubclass(IpsSdkException, Exception)
    
    def test_api_exception_without_body(self):
        """测试 API 异常无响应体"""
        exc = IpsApiException(status_code=500, message="Server Error")
        assert exc.response_body is None
