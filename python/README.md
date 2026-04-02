# IPS SDK Python

Python SDK for IPS Comprehensive Service Platform - OCR, Certificate Detection, Seal Recognition

## Installation

```bash
pip install ips-sdk
```

## Quick Start

```python
from ips_sdk import IpsClient

client = IpsClient()

# 身份证检测
response = client.idcard_preprocess_v3(
    file="idcard.jpg",
    return_ocr_text=True
)

if response.is_success():
    for result in response.data:
        print(f"类型: {result.idcard_type}, 置信度: {result.confidence}")
```

## Development

### Build

```bash
# 构建 wheel 包
uv build

# 安装测试
pip install dist/ips_sdk-1.0.0-py3-none-any.whl
```

### Publish

```bash
# 发布到 Test PyPI（测试）
uv publish --repository testpypi

# 发布到正式 PyPI
uv publish
```

## License

MIT
