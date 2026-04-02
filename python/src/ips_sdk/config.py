"""
IPS SDK Python - 配置类
"""

from typing import Optional


class IpsConfig:
    """SDK 配置"""
    
    def __init__(
        self,
        base_url: str = "http://localhost:8000",
        timeout: float = 30.0,
        api_key: Optional[str] = None
    ):
        self.base_url = base_url
        self.timeout = timeout
        self.api_key = api_key
