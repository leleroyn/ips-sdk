"""
IPS SDK Python - 异常定义
"""

from typing import Optional


class IpsSdkException(Exception):
    """SDK 基础异常"""
    
    def __init__(self, message: str):
        self.message = message
        super().__init__(message)


class IpsApiException(IpsSdkException):
    """API 返回错误异常"""
    
    def __init__(
        self,
        status_code: int,
        message: str,
        response_body: Optional[str] = None
    ):
        self.status_code = status_code
        self.response_body = response_body
        super().__init__(f"[{status_code}] {message}")


class IpsNetworkException(IpsSdkException):
    """网络请求异常"""
    
    def __init__(self, message: str):
        super().__init__(f"网络错误: {message}")
