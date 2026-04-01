# IPS Java SDK

Java SDK for IPS Comprehensive Service Platform.

## 环境要求

- JDK 1.8+
- Maven 3.0+

## 在其他项目中集成 SDK

### 方式一：Maven 本地安装（推荐）

1. 先将 SDK 安装到本地 Maven 仓库：

```bash
cd ips-sdk/java
mvn clean install
```

2. 在你的项目中添加依赖：

```xml
<dependency>
    <groupId>com.ips.sdk</groupId>
    <artifactId>ips-sdk-java</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 方式二：直接引入源码

如果你不想使用 Maven 仓库，可以直接把 SDK 源码复制到你的项目中：

1. 复制以下目录到你的项目中：
   ```
   src/main/java/com/ips/sdk/
   ```
2. 将其加入你的项目源码目录

### 方式三：发布到私有 Maven 仓库

如果你的公司有私有 Maven 仓库（如 Nexus、Artifactory），可以先发布到私有仓库：

```bash
# 发布到私有仓库
mvn clean deploy -DaltReleaseUrl=scp://your-repo/nexus/releases
     -DaltSnapshotUrl=scp://your-repo/nexus/snapshots
```

然后在项目中配置仓库地址并添加依赖。

## 快速开始

```java
// 创建客户端
IpsClient client = new IpsClient();

// 身份证检测 - 使用 InputStream
try (InputStream is = new FileInputStream("idcard.jpg")) {
    IdCardUploadParams params = new IdCardUploadParams()
            .withReturnCorpImage(true)
            .withReturnOcrText(true);
    
    ApiResponse<List<IdCardResult>> response = client.idCardPreprocessV3(is, "idcard.jpg", params);
    
    if (response.isSuccess()) {
        // 成功
        int statusCode = response.getStatusCode();  // 200
        List<IdCardResult> data = response.getData();
    } else {
        // 失败
        int statusCode = response.getStatusCode();  // 错误码
        String error = response.getErrorMessage();
    }
}

// 或使用 File（更简单）
File file = new File("idcard.jpg");
ApiResponse<List<IdCardResult>> response = client.idCardPreprocessV3(file, params);

client.close();
```

## API 接口

| 接口 | 方法 | 说明 |
|-----|------|------|
| 身份证检测 | `idCardPreprocess` / `idCardPreprocessV3` | v1 Base64 / v3 文件上传 |
| 营业执照检测 | `bizLicPreprocess` / `bizLicPreprocessV3` | v1 Base64 / v3 文件上传 |
| 印章检测 | `sealPreprocess` / `sealPreprocessV2` / `sealPreprocessV3` | v1/v2 Base64 / v3 文件上传 |
| 通用证件检测 | `cardDetection` / `cardDetectionV3` | v2 Base64 / v3 文件上传 |
| OCR 识别 | `ocrPreprocessV2` / `ocrPreprocessV3` | v2 Base64 / v3 文件上传 |
| 发票识别 | `invoicePreprocessV2` / `invoicePreprocessV3` | v2 Base64 / v3 文件上传 |
| 手印检测 | `handprintDetectV2` | v2 文件上传 |
| 健康检查 | `healthCheck` | - |

## 文件上传方式

SDK 支持两种文件上传方式：

### 方式一：使用 InputStream（推荐，更通用）

适用于文件流、网络流、字节数组等场景：

```java
// 从文件
try (InputStream is = new FileInputStream("idcard.jpg")) {
    client.idCardPreprocessV3(is, "idcard.jpg", params);
}

// 从网络
URL url = new URL("https://example.com/image.jpg");
try (InputStream is = url.openStream()) {
    client.idCardPreprocessV3(is, "image.jpg", params);
}

// 从字节数组
byte[] bytes = ...;
try (InputStream is = new ByteArrayInputStream(bytes)) {
    client.idCardPreprocessV3(is, "image.jpg", params);
}
```

### 方式二：使用 File（简单）

适用于本地文件：

```java
File file = new File("idcard.jpg");
client.idCardPreprocessV3(file, params);
```

## ApiResponse 响应说明

所有 API 方法都返回 `ApiResponse<T>`：

| 字段 | 说明 |
|-----|------|
| `success` | 是否成功 |
| `statusCode` | HTTP 状态码（200=成功，0=网络错误，-1=SDK错误） |
| `data` | 响应数据（成功时） |
| `errorMessage` | 错误消息（失败时） |

### 状态码参考

| 状态码 | 说明 |
|-------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 认证失败 |
| 404 | 资源不存在 |
| 500 | 服务器错误 |
| 0 | 网络连接失败 |
| -1 | SDK 内部错误 |

## 依赖

- OkHttp 4.12.0
- Jackson 2.16.1
- SLF4J 2.0.9
