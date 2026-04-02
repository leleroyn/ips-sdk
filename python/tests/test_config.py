"""
IPS SDK - 配置测试
"""

import pytest
from ips_sdk import IpsConfig


class TestConfig:
    """配置测试"""
    
    def test_default_config(self):
        """测试默认配置"""
        config = IpsConfig()
        assert config.base_url == "http://localhost:8000"
        assert config.timeout == 30.0
        assert config.api_key is None
    
    def test_custom_base_url(self):
        """测试自定义 base_url"""
        config = IpsConfig(base_url="https://api.example.com")
        assert config.base_url == "https://api.example.com"
    
    def test_custom_timeout(self):
        """测试自定义 timeout"""
        config = IpsConfig(timeout=60.0)
        assert config.timeout == 60.0
    
    def test_custom_api_key(self):
        """测试自定义 api_key"""
        config = IpsConfig(api_key="test-key-123")
        assert config.api_key == "test-key-123"
    
    def test_full_custom_config(self):
        """测试完整自定义配置"""
        config = IpsConfig(
            base_url="https://custom.api.com",
            timeout=120.0,
            api_key="my-secret-key"
        )
        assert config.base_url == "https://custom.api.com"
        assert config.timeout == 120.0
        assert config.api_key == "my-secret-key"
