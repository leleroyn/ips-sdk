# IPS Python SDK 设计报告

## 1. 项目概述

**项目名称**: `ips-sdk-python`
**目标**: 基于 OpenAPI 规范生成与 Java SDK 功能一致的 Python SDK

## 2. 技术栈

| 组件 | 技术选型 | 理由 |
|------|----------|------|
| HTTP 客户端 | `httpx` | 同步/异步支持，现代化 API |
| JSON 处理 | `pydantic` | 数据验证 + 自动生成类型提示 |
| 包管理 | `uv` | 快速、现代的 Python 包管理 |
| 类型提示 | `typing` | 完整的类型标注 |

## 3. 项目结构

```
python/
├── src/
│   └── ips_sdk/
│       ├── __init__.py
│       ├── client.py          # 核心客户端
│       ├── config.py          # 配置类
│       ├── models/
│       │   ├── __init__.py
│       │   ├── request.py     # 请求模型
│       │   └── response.py    # 响应模型
│       ├── exceptions.py      # 异常定义
│       └── http.py            # HTTP 请求封装
├── pyproject.toml
├── uv.lock
└── README.md
```

## 4. 核心设计

### 4.1 客户端设计

```python
from ips_sdk import IpsClient

client = IpsClient(base_url="http://localhost:8000")

# 文件上传方式 (推荐)
response = client.idcard_preprocess_v3(
    file_path="idcard.jpg",
    return_corp_image=True,
    return_ocr_text=True
)

# Base64 方式
response = client.idcard_preprocess(
    image_base64="...",
    return_corp_image=True
)
```

### 4.2 响应模型

```python
from ips_sdk.models import ApiResponse

response = client.idcard_preprocess_v3(file_path="idcard.jpg")

if response.is_success():
    results = response.data  # List[IdCardResult]
else:
    print(response.error_message)
```

### 4.3 模型类 (Pydantic)

```python
from pydantic import BaseModel, Field
from typing import Optional

class IdCardResult(BaseModel):
    corp_image_base64: Optional[str] = Field(None, alias="corp_image_base64")
    confidence: Optional[float] = None
    idcard_type: Optional[int] = Field(None, alias="idcard_type")
    ocr_text: Optional[str] = Field(None, alias="ocr_text")
```

## 5. API 覆盖

### 5.1 v1 接口 (Base64 方式)

| 端点 | 请求 | 响应 |
|------|------|------|
| `/api/v1/idcard/preprocess` | `IdCardRequest` | `List[IdCardResult]` |
| `/api/v1/bizlic/preprocess` | `BizLicRequest` | `List[BizLicResult]` |
| `/api/v1/seal/preprocess` | `SealRequest` | `List[SealResult]` |

### 5.2 v2 接口

| 端点 | 请求 | 响应 |
|------|------|------|
| `/api/v2/card/det` | `CardRequest` | `CardResult` |
| `/api/v2/ocr/preprocess` | `OcrRequest` | `List[OcrResult]` |
| `/api/v2/seal/preprocess` | `SealRequest` | `List[SealResult]` |
| `/api/v2/invoice/preprocess` | `InvoiceRequest` | `List[InvoiceResult]` |
| `/api/v2/handprint/detect` | `file`, `position` | `HandprintResult` |

### 5.3 v3 接口 (文件上传)

| 端点 | 请求参数 | 响应 |
|------|----------|------|
| `/api/v3/idcard/preprocess` | `file`, `return_corp_image`, `return_ocr_text`, `init_confidence`, `resize`, `back_ground` | `List[IdCardResult]` |
| `/api/v3/bizlic/preprocess` | `file`, `return_corp_image`, `return_ocr_text`, `init_confidence`, `resize`, `back_ground` | `List[BizLicResult]` |
| `/api/v3/invoice/preprocess` | `file`, `return_corp_image`, `return_ocr_text`, `init_confidence`, `resize`, `back_ground` | `List[InvoiceResult]` |
| `/api/v3/seal/preprocess` | `file`, `return_seal_image`, `return_ocr_text`, `init_confidence`, `resize`, `back_ground` | `List[SealResult]` |
| `/api/v3/card/det` | `file`, `return_corp_image` | `CardResult` |
| `/api/v3/ocr/preprocess` | `file`, `app_id`, `resize`, `use_cls`, `format` | `OcrResult` |

### 5.4 健康检查

| 端点 | 响应 |
|------|------|
| `/health` | `{status: str, timestamp: int}` |

## 6. Schema 定义

### 6.1 请求模型

```python
class ToolConfig(BaseModel):
    """工具配置"""
    init_confidence: Optional[float] = None       # 初始置信度阈值
    resize: Optional[bool] = None                 # 是否调整图像大小
    back_ground: Optional[bool] = None            # 是否添加背景

class IdCardRequest(BaseModel):
    """身份证检测请求"""
    image_base64: str                             # 图像Base64编码
    return_corp_image: Optional[bool] = None      # 是否返回裁剪图像
    return_ocr_text: Optional[bool] = None        # 是否返回OCR文本
    tool: Optional[ToolConfig] = None             # 工具配置

class BizLicRequest(BaseModel):
    """营业执照检测请求"""
    image_base64: str
    return_corp_image: Optional[bool] = None
    return_ocr_text: Optional[bool] = None
    tool: Optional[ToolConfig] = None

class SealRequest(BaseModel):
    """印章检测请求"""
    image_base64: str
    file_type: Optional[str] = None               # 文件类型 (image/pdf)
    return_seal_image: Optional[bool] = None      # 是否返回印章图像
    return_ocr_text: Optional[bool] = None
    tool: Optional[ToolConfig] = None

class CardRequest(BaseModel):
    """通用证件检测请求"""
    image_base64: str
    return_corp_image: Optional[bool] = None

class InvoiceRequest(BaseModel):
    """发票识别请求"""
    image_base64: str
    file_type: Optional[str] = None
    return_corp_image: Optional[bool] = None
    return_ocr_text: Optional[bool] = None
    tool: Optional[ToolConfig] = None

class OcrRequest(BaseModel):
    """OCR识别请求"""
    image_base64: str
    file_type: Optional[str] = None
    return_image: Optional[bool] = None
    return_text: Optional[bool] = None
```

### 6.2 响应模型

```python
class IdCardResult(BaseModel):
    """身份证检测结果"""
    corp_image_base64: Optional[str] = None        # 裁剪后的身份证图像
    confidence: Optional[float] = None            # 置信度
    idcard_type: Optional[int] = None            # 0:正面 1:背面
    ocr_text: Optional[str] = None               # OCR识别文本

class BizLicResult(BaseModel):
    """营业执照检测结果"""
    corp_image_base64: Optional[str] = None
    confidence: Optional[float] = None
    bizlic_type: Optional[int] = None           # 营业执照类型
    ocr_text: Optional[str] = None

class SealResult(BaseModel):
    """印章检测结果"""
    seal_image_base64: Optional[str] = None      # 印章图像
    confidence: Optional[float] = None
    seal_type: Optional[int] = None              # 印章类型
    ocr_result: Optional[str] = None             # OCR结果

class CardResult(BaseModel):
    """通用证件检测结果"""
    corp_image_base64: Optional[str] = None
    card_info: Optional[CardInfo] = None
    fork: Optional[bool] = None

class CardInfo(BaseModel):
    """证件信息"""
    card_type: Optional[int] = None
    confidence: Optional[float] = None

class InvoiceResult(BaseModel):
    """发票识别结果"""
    corp_image_base64: Optional[str] = None
    confidence: Optional[float] = None
    invoice_type: Optional[int] = None          # 发票类型
    ocr_text: Optional[str] = None
    source_text: Optional[str] = None            # 源文本

class OcrResult(BaseModel):
    """OCR识别结果"""
    ocr_text: Optional[str] = None               # OCR内容

class HandprintResult(BaseModel):
    """手印检测结果"""
    has_handprint: Optional[bool] = None         # 是否存在手印

class HealthStatus(BaseModel):
    """健康检查状态"""
    status: Optional[str] = None
    timestamp: Optional[int] = None

class ApiResponse(BaseModel):
    """统一响应包装"""
    success: bool
    status_code: int
    data: Optional[Any] = None
    error_message: Optional[str] = None
```

## 7. 关键特性

### 6.1 统一的异常处理

```python
class IpsApiException(Exception):
    status_code: int
    message: str
    response_body: Optional[str]

class IpsNetworkException(Exception):
    message: str

class IpsSdkException(Exception):
    message: str
```

### 6.2 文件上传支持

```python
from ips_sdk.http import FileLike

# 方式一：文件路径 (str/pathlib.Path)
client.idcard_preprocess_v3(file="idcard.jpg")
client.idcard_preprocess_v3(file=Path("idcard.pdf"))

# 方式二：bytes
with open("idcard.jpg", "rb") as f:
    image_bytes = f.read()
client.idcard_preprocess_v3(file=image_bytes, filename="idcard.jpg")

# 方式三：文件对象 (IO[bytes])
with open("idcard.jpg", "rb") as f:
    client.idcard_preprocess_v3(file=f, filename="idcard.jpg")

# 方式四：FileLike 对象 (含 filename)
client.idcard_preprocess_v3(
    file=FileLike(open("idcard.jpg", "rb"), filename="idcard.jpg")
)
```

**FileLike 统一封装**：

```python
from typing import Union
from pathlib import Path

FileInput = Union[str, Path, bytes, IO[bytes], "FileLike"]

class FileLike:
    """文件输入的统一封装"""
    def __init__(
        self,
        file: Union[IO[bytes], bytes],
        filename: str,
        content_type: Optional[str] = None
    ):
        self.file = file if isinstance(file, (IO, bytes)) else None
        self.filename = filename
        self.content_type = content_type or self._guess_content_type(filename)
    
    @staticmethod
    def _guess_content_type(filename: str) -> str:
        """根据文件扩展名推断 MIME 类型"""
        ext = Path(filename).suffix.lower()
        return {
            ".jpg": "image/jpeg",
            ".jpeg": "image/jpeg",
            ".png": "image/png",
            ".pdf": "application/pdf",
            ".gif": "image/gif",
            ".bmp": "image/bmp",
            ".webp": "image/webp",
        }.get(ext, "application/octet-stream")
```

### 6.3 异步支持 (可选)

```python
import asyncio
from ips_sdk import AsyncIpsClient

async def main():
    async with AsyncIpsClient() as client:
        response = await client.idcard_preprocess_v3(file_path="idcard.jpg")
```

## 7. 依赖清单

```toml
[project]
name = "ips-sdk"
version = "1.0.0"
requires-python = ">=3.9"
dependencies = [
    "httpx>=0.25.0",
    "pydantic>=2.0.0",
]

[project.optional-dependencies]
dev = [
    "pytest>=7.0.0",
    "pytest-asyncio>=0.21.0",
    "ruff>=0.1.0",
]
```

## 8. 实现计划

| 阶段 | 任务 | 优先级 |
|------|------|--------|
| 1 | 项目初始化 (uv, pyproject.toml) | P0 |
| 2 | 核心客户端 + HTTP 封装 | P0 |
| 3 | 请求/响应模型 (Pydantic) | P0 |
| 4 | 异常定义 | P0 |
| 5 | v1 API 实现 (Base64) | P0 |
| 6 | v2 API 实现 | P0 |
| 7 | v3 API 实现 (文件上传) | P0 |
| 8 | 健康检查 | P1 |
| 9 | 示例代码 | P1 |
| 10 | 单元测试 | P2 |

## 9. 代码示例

### 身份证检测

```python
from ips_sdk import IpsClient

client = IpsClient()

# 方式一：文件路径 (推荐)
response = client.idcard_preprocess_v3(
    file="idcard.jpg",
    return_corp_image=True,
    return_ocr_text=True
)

# 方式二：bytes
response = client.idcard_preprocess_v3(
    file=image_bytes,
    filename="idcard.jpg",
    return_ocr_text=True
)

# 方式三：文件对象
with open("idcard.jpg", "rb") as f:
    response = client.idcard_preprocess_v3(file=f, filename="idcard.jpg")

if response.is_success():
    for result in response.data:
        print(f"类型: {result.idcard_type}, 置信度: {result.confidence}")
```

### 营业执照检测

```python
response = client.bizlic_preprocess_v3(
    file="bizlic.jpg",
    init_confidence=0.8,  # 自定义置信度阈值
    return_ocr_text=True
)
```

### OCR 识别

```python
response = client.ocr_preprocess_v3(
    file="document.pdf",
    format="txt"
)
```

## 10. 注意事项

1. **文件大小限制**: 文件上传建议不超过 10MB
2. **MIME 类型**: 自动根据文件扩展名判断
3. **超时设置**: 默认 30 秒，支持自定义
4. **连接池**: 使用 httpx 内置连接池
5. **重试机制**: 可选配置 (默认关闭)
6. **测试数据**: 需要准备测试图片/PDF 文件

## 11. 测试用例设计

### 11.1 单元测试 (tests/test_models.py)

| 测试用例 | 输入 | 预期结果 |
|----------|------|----------|
| `test_idcard_request_creation` | `image_base64="..."` | 请求对象创建成功 |
| `test_idcard_result_parsing` | `{"confidence": 0.95, ...}` | 正确解析所有字段 |
| `test_seal_result_parsing` | `{"seal_type": 1, ...}` | seal_type, seal_image_base64 |
| `test_invoice_result_parsing` | `{"invoice_type": 2, ...}` | invoice_type, source_text |
| `test_ocr_result_nullable` | `{ocr_text: null}` | 可空字段正确处理 |
| `test_api_response_success` | `success=True` | `is_success()=True` |
| `test_api_response_failure` | `success=False` | `is_success()=False` |
| `test_card_info_parsing` | `{"card_type": 1, ...}` | 正确嵌套解析 |

### 11.2 文件输入测试 (tests/test_file_input.py)

| 测试用例 | 输入 | 预期结果 |
|----------|------|----------|
| `test_file_path_string` | `"test.jpg"` | filename="test.jpg" |
| `test_file_path_pathlib` | `Path("test.jpg")` | filename="test.jpg" |
| `test_file_bytes` | `b"fake data"` | read() 返回 bytes |
| `test_file_io_object` | `open("x", "rb")` | 正常读取 |
| `test_content_type_jpg` | `"test.jpg"` | "image/jpeg" |
| `test_content_type_pdf` | `"doc.pdf"` | "application/pdf" |
| `test_content_type_unknown` | `"x.abc"` | "application/octet-stream" |
| `test_file_input_auto_filename` | `bytes + "x.jpg"` | 自动推断 filename |

### 11.3 配置测试 (tests/test_config.py)

| 测试用例 | 输入 | 预期结果 |
|----------|------|----------|
| `test_default_config` | 无参数 | base_url="http://localhost:8000" |
| `test_custom_config` | `base_url="..."` | 使用自定义 URL |
| `test_timeout_config` | `timeout=60` | 超时 60 秒 |

### 11.4 API 集成测试 (tests/test_api.py)

| 测试用例 | 端点 | 预期结果 |
|----------|------|----------|
| `test_health_check` | GET /health | status_code=200, data.status="healthy" |
| `test_idcard_preprocess_v3_path` | POST /api/v3/idcard/preprocess | 返回 IdCardResult 列表 |
| `test_idcard_preprocess_v3_bytes` | bytes 输入 | 正常工作 |
| `test_idcard_preprocess_v3_io` | 文件对象输入 | 正常工作 |
| `test_idcard_preprocess_v3_all_params` | 全部参数 | init_confidence=0.5 等 |
| `test_bizlic_preprocess_v3` | POST /api/v3/bizlic/preprocess | 返回 BizLicResult |
| `test_bizlic_preprocess_v3_init_confidence` | init_confidence=0.8 | 使用 0.8 阈值 |
| `test_seal_preprocess_v3` | POST /api/v3/seal/preprocess | 返回 SealResult |
| `test_seal_preprocess_v3_return_seal` | return_seal_image=True | seal_image_base64 非空 |
| `test_card_detection_v3` | POST /api/v3/card/det | 返回 CardResult |
| `test_ocr_preprocess_v3` | POST /api/v3/ocr/preprocess | 返回 OcrResult |
| `test_ocr_preprocess_v3_format` | format="txt" | 使用 txt 格式 |
| `test_invoice_preprocess_v3` | POST /api/v3/invoice/preprocess | 返回 InvoiceResult |
| `test_handprint_detect_v2` | POST /api/v2/handprint/detect | 返回 HandprintResult |
| `test_handprint_detect_v2_position` | position="left" | 正确传递参数 |

### 11.5 错误处理测试 (tests/test_error.py)

| 测试用例 | 场景 | 预期结果 |
|----------|------|----------|
| `test_network_error` | 服务器不可达 | 抛出 IpsNetworkException |
| `test_api_error_response` | 返回错误码 | IpsApiException |
| `test_invalid_file_path` | 文件不存在 | 抛出 FileNotFoundError |
| `test_timeout_handling` | 超时 | 抛出 IpsNetworkException |

### 11.6 测试数据要求

```
tests/
├── data/
│   ├── idcard_front.jpg      # 身份证正面
│   ├── idcard_back.jpg       # 身份证背面
│   ├── bizlic.jpg            # 营业执照
│   ├── seal.jpg              # 印章图片
│   ├── card.jpg              # 通用证件
│   ├── invoice.pdf           # 发票 PDF
│   ├── doc.pdf              # 文档 PDF
│   └── handprint.jpg        # 手印图片
└── conftest.py              # pytest fixtures
```

---

**审核意见**: ⏳ 待审核

请确认以上设计是否符合预期，确认后我将开始实现。
