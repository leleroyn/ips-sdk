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

## License

MIT
