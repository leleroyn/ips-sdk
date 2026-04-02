---
name: sdk-development
description: |
  SDK 开发工作流 - 根据 OpenAPI 规范创建多语言 SDK。
  
  当用户说"创建 SDK"、"生成 SDK"、"SDK 开发"时触发。
  适用于需要根据 API 规范生成客户端 SDK 的场景。
  
  支持 Java (Maven) 和 Python (uv) 两种语言。
---

# SDK 开发工作流

根据 OpenAPI 规范创建多语言 SDK，支持 Java 和 Python。

## 输入参数

开始前，需要向用户确认以下信息：

| 参数 | 说明 | 示例 |
|------|------|------|
| `PROJECT_NAME` | 项目名称 | `payment-api` |
| `API_SPEC` | OpenAPI 规范文件路径 | `openapi.json`, `./api/spec.yaml` |
| `LANGUAGES` | 目标语言 | `java,python` 或 `java` |
| `GROUP_ID` | Java groupId | `com.example` |
| `ARTIFACT_ID` | Java artifactId | `payment-api` |
| `PACKAGE_NAME` | Python 包名 | `payment_api` |

## 工作流程

### 1. 分析 OpenAPI 规范

读取 API 规范文件，分析：
- API 端点（paths）
- 请求模型（requestBody, parameters）
- 响应模型（responses, schemas）
- 认证方式（securitySchemes）

#### 执行要点

1. **读取 OpenAPI 文件**
   - 读取 `openapi.json` 或 `openapi.yaml`
   - 获取 `info.title` 作为项目描述
   - 获取 `servers[0].url` 作为 baseUrl

2. **遍历端点**
   - 遍历 `paths` 对象获取所有 API 端点
   - 每个端点包含：path, method, operationId, requestBody, responses

3. **提取数据模型**
   - 遍历 `components/schemas` 获取所有数据模型
   - 从 `properties` 提取字段名和类型
   - 处理嵌套对象（$ref 引用）

4. **识别公共配置**
   - 提取跨端点共享的参数（如分页配置、公共查询参数）
   - 识别请求类型（JSON / multipart）

### 2. 创建项目结构

```
{PROJECT_NAME}/
├── java/                              # Java SDK（根据 LANGUAGES 生成）
│   ├── pom.xml
│   └── src/main/java/{GROUP_ID}/
│       └── {ARTIFACT_ID}/
│           ├── {PROJECT_NAME}Client.java
│           ├── config/
│           ├── http/
│           ├── model/
│           └── util/
└── python/                           # Python SDK（根据 LANGUAGES 生成）
    ├── pyproject.toml
    ├── src/{PACKAGE_NAME}/
    │   ├── __init__.py
    │   ├── client.py
    │   ├── config.py
    │   ├── exceptions.py
    │   └── models/
    │       ├── __init__.py
    │       ├── request.py
    │       └── response.py
    └── tests/
```

### 3. Java SDK 实现

#### 执行要点

1. **创建 pom.xml**
   - 设置 groupId、artifactId、version
   - 添加 OkHttp、Jackson、Lombok 依赖

2. **生成模型类**
   - 为每个 schema 创建请求类和响应类
   - 使用 Lombok 注解：`@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`
   - 字段类型映射：
     - `string` → `String`
     - `integer` → `Integer`
     - `boolean` → `Boolean`
     - `number` → `Double`

3. **生成客户端类**
   - 类名：`{PROJECT_NAME}Client`
   - 实现 `AutoCloseable` 接口
   - 认证方式：从 OpenAPI 的 securitySchemes 获取，常见为 API Key 放在 header 或 query
   - 方法命名：端点 path + operationId，如 `/api/v1/user/create` → `userCreateV1`
   - multipart 用 `@Multipart` 注解
   - JSON 用 `@Body` 注解

4. **异常处理**
   - 自定义异常基类：`{PROJECT_NAME}Exception`
   - 网络异常：`{PROJECT_NAME}NetworkException`
   - API 错误：`{PROJECT_NAME}ApiException`
   - 使用函数式接口封装异常处理逻辑

5. **工具类（按需创建）**
   - 根据实际需要创建必要的工具类
   - 如有 Base64 编码需求，创建 Base64Utils
   - 如有文件处理需求，创建 FileUtils

#### pom.xml 模板

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project>
    <groupId>{GROUP_ID}</groupId>
    <artifactId>{ARTIFACT_ID}-java</artifactId>
    <version>1.0.0</version>
    
    <properties>
        <okhttp.version>4.12.0</okhttp.version>
        <jackson.version>2.16.1</jackson.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>com.squareup.okhttp3</groupId>
            <artifactId>okhttp</artifactId>
            <version>${okhttp.version}</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.30</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
</project>
```

#### 模型类 - Lombok 简化

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class {MODEL_NAME}Request {
    // 字段映射 OpenAPI schema
}
```

#### {PROJECT_NAME}Client 模式

```java
public class {PROJECT_NAME}Client implements AutoCloseable {
    private final OkHttpClient httpClient;
    private final String baseUrl;
    private final String apiKey;
    
    public <T> T execute(Request request, Class<T> responseClass) {
        try (Response response = httpClient.newCall(request).execute()) {
            return parseResponse(response, responseClass);
        } catch (IOException e) {
            throw new {PROJECT_NAME}NetworkException("Network error", e);
        }
    }
}
```

#### 优化建议（按需使用）

- 如有 MIME 类型判断需求，使用 `HashSet` 缓存
- 如有大文件 Base64 编码需求，使用流式处理
- HttpClient 连接池配置

### 4. Python SDK 实现

#### 执行要点

1. **创建 pyproject.toml**
   - 设置 name、version、requires-python
   - 添加 httpx、pydantic 依赖
   - 配置 hatchling 构建后端

2. **生成模型类**
   - 使用 Pydantic v2：`BaseModel`, `ConfigDict`, `Field`
   - 蛇形/驼峰命名转换用 `validation_alias`
   - `model_config = ConfigDict(populate_by_name=True)`
   - 序列化用 `model_dump(by_alias=True)`

3. **生成客户端类**
   - 类名：`{PROJECT_NAME}Client`
   - 使用 httpx 发送请求
   - 实现上下文管理器：`__enter__`, `__exit__`
   - 认证方式：从 OpenAPI 的 securitySchemes 获取
   - 方法命名：蛇形命名，如 `user_create_v1`

4. **文件上传（仅当有 multipart 请求时）**
   - 创建 `FileInput` 类支持多种输入类型
   - 支持：`str`, `Path`, `bytes`, `IO[bytes]`

5. **异常处理**
   - 自定义异常类：`{PROJECT_NAME}Exception`
   - 网络异常：`{PROJECT_NAME}NetworkException`
   - API 错误：`{PROJECT_NAME}ApiException`
   - 使用 httpx 异常链

#### pyproject.toml 模板

```toml
[project]
name = "{PACKAGE_NAME}"
version = "1.0.0"
requires-python = ">=3.9"

dependencies = [
    "httpx>=0.25.0",
    "pydantic>=2.0.0",
]

[build-system]
requires = ["hatchling"]
build-backend = "hatchling.build"
```

#### 模型类 - Pydantic v2 最佳实践

```python
from pydantic import BaseModel, ConfigDict, Field

class {MODEL_NAME}Request(BaseModel):
    model_config = ConfigDict(populate_by_name=True)
    
    # 使用 validation_alias 处理蛇形/驼峰命名
    field_name: str = Field(
        default=...,
        validation_alias="fieldName"
    )
    
    def to_dict(self) -> dict:
        return self.model_dump(by_alias=True, exclude_none=True)
```

#### {PROJECT_NAME}Client 模式

```python
class {PROJECT_NAME}Client:
    def __init__(self, api_key: str, base_url: str, timeout: float = 30.0):
        self.api_key = api_key
        self.base_url = base_url.rstrip("/")
        self._client = httpx.Client(timeout=timeout)
    
    def _request(self, method: str, endpoint: str, **kwargs) -> dict:
        url = f"{self.base_url}{endpoint}"
        # 认证方式根据 OpenAPI securitySchemes 确定
        headers = {"X-API-Key": self.api_key}
        response = self._client.request(method, url, headers=headers, **kwargs)
        response.raise_for_status()
        return response.json()
    
    def close(self):
        self._client.close()
    
    def __enter__(self):
        return self
    
    def __exit__(self, *args):
        self.close()
```

#### FileInput（仅当有 multipart 请求时）

```python
from pathlib import Path
from typing import Union, IO

class FileInput:
    SUPPORTED_TYPES = (str, Path, bytes, IO)
    
    def __init__(self, value: Union[str, Path, bytes, IO]):
        if not isinstance(value, self.SUPPORTED_TYPES):
            raise TypeError(f"Expected {self.SUPPORTED_TYPES}, got {type(value)}")
        self.value = value
    
    def to_bytes(self) -> bytes:
        if isinstance(self.value, bytes):
            return self.value
        elif isinstance(self.value, (str, Path)):
            return Path(self.value).read_bytes()
        else:
            return self.value.read()
```

### 5. 测试策略

#### 执行要点

1. **Python 测试**
   - 使用 pytest + pytest-mock
   - Mock httpx.Client.request 方法
   - 测试各端点、异常处理

2. **Java 测试**
   - 使用 JUnit 5 + Mockito
   - Mock OkHttpClient 响应
   - 测试模型序列化、客户端方法

3. **覆盖要求**
   - 每个端点至少一个测试
   - 测试成功和失败场景
   - 如有 multipart 请求，测试文件上传功能

#### Python 测试模板

```python
import pytest
from unittest.mock import Mock, patch

class TestApiMocked:
    @pytest.fixture
    def client(self):
        return {PROJECT_NAME}Client(api_key="test-key")
    
    @patch("httpx.Client.request")
    def test_api_call(self, mock_request, client):
        mock_request.return_value = Mock(
            status_code=200,
            json=lambda: {"success": True, "data": {}}
        )
        
        result = client.some_endpoint(param="value")
        assert result.is_success()
```

#### Java 测试模板

```java
@Test
void testApiCall() {
    {PROJECT_NAME}Client client = new {PROJECT_NAME}Client("test-key");
    when(httpClient.newCall(any()).execute())
        .thenReturn(mockResponse);
    
    var result = client.someEndpoint(param);
    assertTrue(result.isSuccess());
}
```

### 6. 构建发布

#### 执行要点

1. **Java 构建**
   - 运行 `mvn clean compile` 验证编译
   - 运行 `mvn test` 验证测试
   - 运行 `mvn package` 生成 jar
   - 运行 `mvn install` 安装到本地仓库

2. **Python 构建**
   - 运行 `uv run pytest` 验证测试
   - 运行 `uv build` 生成 wheel
   - 运行 `pip install dist/*.whl` 本地测试

#### Java
```bash
cd java
mvn clean package      # 构建 jar
mvn install            # 安装到本地仓库
```

#### Python
```bash
cd python
uv build               # 构建 wheel
uv pip install -e .    # 开发模式安装
uv publish             # 发布到 PyPI
```

### 7. 文档

#### 执行要点

1. **根目录 README.md**
   - 多语言 SDK 概览
   - 安装方式对比表
   - 快速开始示例

2. **语言目录 README.md**
   - 语言特定安装命令
   - 详细使用示例
   - API 文档链接

3. **代码注释**
   - Java: JavaDoc 注释
   - Python: docstring

#### README.md 模板

```markdown
# {PROJECT_NAME} SDK

{Description}

## 安装

{根据 LANGUAGES 生成安装说明}

## 快速开始

{根据选择的语言生成示例代码}

## License

MIT
```

---

## 输出清单

- [ ] 项目结构已创建
- [ ] Java SDK 代码（如果选择 Java）
- [ ] Python SDK 代码（如果选择 Python）
- [ ] 单元测试
- [ ] README.md

## 注意事项

1. **动态替换**：所有占位符 `{PROJECT_NAME}`, `{GROUP_ID}` 等需替换为实际项目值
2. **API 兼容性**：确保生成的 SDK 与 OpenAPI 规范完全一致
3. **测试覆盖**：核心功能必须有测试覆盖
4. **错误处理**：网络错误、API 错误需正确处理和抛异常
