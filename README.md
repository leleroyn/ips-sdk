# IPS SDK

Multi-language SDK for IPS Comprehensive Service Platform - OCR, Certificate Detection, Seal Recognition

## Supported Languages

| Language | Version | Status |
|----------|---------|--------|
| Java | 1.0.0 | ✅ Stable |
| Python | 1.0.0 | ✅ Stable |
| Rust CLI | 1.0.3 | ✅ Stable |

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

### Rust CLI

命令名 `ips`, 二进制由 GitHub Actions 自动构建并发布: [Releases](https://github.com/leleroyn/ips-sdk/releases) (`ips` Linux x86_64 静态 / `ips.exe` Windows x86_64)。

```bash
# Linux
curl -fsSL -o ips https://github.com/leleroyn/ips-sdk/releases/download/v1.0.3/ips
chmod +x ips
export IPS_BASE_URL=http://112.94.31.26:18080/ips

./ips idcard idcard.jpg                 # 本地文件
./ips idcard https://example.com/a.png  # 远程 URL
./ips seal photo.jpg --init-confidence 0.7
./ips ocr doc.pdf --format text
```

特性:
- 二级命令: `idcard` / `bizlic` / `invoice` / `seal` / `ocr` / `card` / `handprint`, 固定使用最新接口版本
- 幂等缓存: 相同命令+参数+文件内容只请求服务器一次, 重复调用直接返回缓存 (默认 `~/.ips/cache`, `IPS_CACHE_DIR` 可覆盖, `--no-cache` 强制刷新)
- 裁剪图固定不返回, OCR 文本固定返回 (idcard/bizlic/invoice/seal), 输出为格式化 JSON

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

### Rust CLI

```bash
cd rust
cargo build --release       # 产物: target/release/ips(.exe)
cargo test --release        # 单元测试
```

> Windows 本机用 GNU 工具链时, 若源码在中文路径, 需设置纯 ASCII 的 `CARGO_TARGET_DIR`。

## License

MIT
