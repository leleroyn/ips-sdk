"""
IPS SDK - 测试配置
"""

import sys
from pathlib import Path

# 添加 src 到路径
sys.path.insert(0, str(Path(__file__).parent.parent / "src"))

import pytest

from ips_sdk import IpsClient, IpsConfig


@pytest.fixture
def client():
    """创建测试客户端"""
    config = IpsConfig(base_url="http://localhost:8000", timeout=30.0)
    return IpsClient(config)


@pytest.fixture
def test_data_dir():
    """测试数据目录"""
    return Path(__file__).parent / "data"


@pytest.fixture
def sample_image_bytes():
    """样本图片字节数据"""
    # 创建一个最小的有效 JPEG 头部 (用于测试)
    return b"\xff\xd8\xff\xe0\x00\x10JFIF\x00\x01\x01\x00\x00\x01\x00\x01\x00\x00\xff\xdb"
