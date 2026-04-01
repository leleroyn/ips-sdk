# 项目记忆

## 2026-04-01: 生成 Java SDK

根据 `openapi.json` 规范文件，在 `java/` 目录下成功生成了完整的 Java SDK 项目。

### 项目结构

```
java/
├── pom.xml                           # Maven 配置
├── README.md                          # 使用文档
├── .gitignore                         # Git 忽略文件
└── src/main/java/com/ips/sdk/
    ├── IpsClient.java                # 主客户端类
    ├── config/IpsConfig.java         # 配置类
    ├── exception/                    # 异常类
    │   ├── IpsSdkException.java
    │   ├── IpsApiException.java
    │   └── IpsNetworkException.java
    ├── http/HttpClient.java          # HTTP 客户端
    ├── model/                        # 数据模型
    │   ├── ToolConfig.java
    │   ├── IdCardRequest.java / IdCardResult.java
    │   ├── BizLicRequest.java / BizLicResult.java
    │   ├── SealRequest.java / SealResult.java
    │   ├── CardRequest.java / CardResult.java / CardInfo.java
    │   ├── OcrRequest.java / OcrResult.java
    │   ├── InvoiceRequest.java / InvoiceResult.java
    │   ├── HandprintRequest.java / HandprintResult.java
    │   ├── HealthStatus.java
    │   └── upload/                    # 上传参数类
    │       ├── IdCardUploadParams.java
    │       ├── BizLicUploadParams.java
    │       ├── SealUploadParams.java
    │       └── InvoiceUploadParams.java
    ├── util/                         # 工具类
    │   ├── Base64Utils.java
    │   └── FileUtils.java
    └── example/SdkExample.java       # 使用示例
```

### SDK 功能

- ✅ 身份证检测 (v1 Base64, v3 文件上传)
- ✅ 营业执照检测 (v1 Base64, v3 文件上传)
- ✅ 印章检测 (v1, v2 支持 PDF, v3 文件上传)
- ✅ 通用证件检测 (v2 Base64, v3 文件上传)
- ✅ 通用 OCR 识别 (v2, 支持 PDF)
- ✅ 发票识别 (v2 支持 PDF, v3 文件上传)
- ✅ 手印检测 (v2)
- ✅ 健康检查

### 技术栈

- Java 1.8+
- OkHttp 4.12.0 (HTTP 客户端)
- Jackson 2.16.1 (JSON 处理)
- SLF4J 2.0.9 (日志)
- Lombok 1.18.30 (可选)

### 简化版本 (2026-04-01)

简化了异常处理机制，**所有方法直接返回 `ApiResponse`**，无需 try-catch：

```java
// 调用 API
ApiResponse<List<IdCardResult>> response = client.idCardPreprocess(request);

// 检查结果
if (response.isSuccess()) {
    int statusCode = response.getStatusCode();  // 200
    List<IdCardResult> data = response.getData();
} else {
    int statusCode = response.getStatusCode();  // 错误码
    String error = response.getErrorMessage();
}
```

### 文件上传方式 (2026-04-01)

文件上传支持两种方式：
1. **InputStream**（推荐）：更通用，支持文件流、网络流、字节数组
2. **File**：简单直接，适用于本地文件

```java
// InputStream 方式
try (InputStream is = new FileInputStream("idcard.jpg")) {
    client.idCardPreprocessV3(is, "idcard.jpg", params);
}

// File 方式（内部自动转 InputStream）
File file = new File("idcard.jpg");
client.idCardPreprocessV3(file, params);
```

### API 变更 (2026-04-01)

根据更新后的 openapi.json：

1. **手印检测** 改为文件上传方式
   - 新方法: `handprintDetectV2(File file, String position)`
   - 路径: `/api/v2/handprint/detect`

2. **新增 OCR v3 接口**
   - 新方法: `ocrPreprocessV3(File file, OcrUploadParams params)`
   - 路径: `/api/v3/ocr/preprocess`
   - 新增 `OcrUploadParams` 参数类

### 状态码说明

- **200**: 成功
- **4xx**: 客户端错误
- **5xx**: 服务器错误
- **0**: 网络连接失败
- **-1**: SDK 内部错误
