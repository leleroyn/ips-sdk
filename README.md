# IPS SDK

Multi-language SDK for IPS Comprehensive Service Platform - OCR, Certificate Detection, Seal Recognition

## Supported Languages

| Language | Version | Status |
|----------|---------|--------|
| Java | 1.0.0 | ✅ Stable |
| Python | 1.0.0 | ✅ Stable |
| Rust CLI | 1.0.0 | ✅ Stable |

## Features

- **身份证识别** - ID Card Recognition
- **营业执照识别** - Business License Detection
- **印章识别** - Seal Detection
- **发票识别** - Invoice OCR
- **通用OCR** - General OCR
- **手印检测** - Handprint Detection

## Quick Start

### Java SDK

```xml
<dependency>
    <groupId>com.ips.sdk</groupId>
    <artifactId>ips-sdk-java</artifactId>
    <version>1.0.0</version>
</dependency>
```

```java
IpsClient client = new IpsClient("your-api-key");

IdCardRequest request = IdCardRequest.builder()
    .filePath("idcard.jpg")
    .returnOcrText(true)
    .build();

ApiResponse<List<IdCardResult>> response = client.idcardPreprocessV3(request);

if (response.isSuccess()) {
    for (IdCardResult result : response.getData()) {
        System.out.println("类型: " + result.getIdcardType());
    }
}
```

### Python SDK

```bash
pip install ips-sdk
```

```python
from ips_sdk import IpsClient

client = IpsClient(api_key="your-api-key")

response = client.idcard_preprocess_v3(
    file="idcard.jpg",
    return_ocr_text=True
)

if response.is_success():
    for result in response.data:
        print(f"类型: {result.idcard_type}")
```

## Project Structure

```
ips-sdk/
├── java/              # Java SDK (Maven)
├── python/             # Python SDK (uv)
├── rust/               # Rust CLI (cargo)
├── openapi.json        # OpenAPI Specification
└── README.md
```

## Development

### Java SDK

```bash
cd java
mvn clean package
mvn install  # Local installation
```

### Python SDK

```bash
cd python
uv build                    # Build wheel
uv pip install -e .         # Development install
uv run pytest tests/        # Run tests
```

## License

MIT
