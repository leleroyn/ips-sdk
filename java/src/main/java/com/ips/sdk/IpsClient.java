package com.ips.sdk;

import com.ips.sdk.config.IpsConfig;
import com.ips.sdk.exception.IpsApiException;
import com.ips.sdk.exception.IpsNetworkException;
import com.ips.sdk.exception.IpsSdkException;
import com.ips.sdk.http.HttpClient;
import com.ips.sdk.model.*;
import com.ips.sdk.model.upload.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * IPS SDK 客户端
 * 所有方法都返回 ApiResponse，包含状态码和数据
 */
public class IpsClient {
    
    private static final Logger logger = LoggerFactory.getLogger(IpsClient.class);
    
    private final HttpClient httpClient;
    
    public IpsClient(IpsConfig config) {
        this.httpClient = new HttpClient(config);
    }
    
    public IpsClient() {
        this(new IpsConfig());
    }
    
    /**
     * 执行请求并统一处理异常
     */
    private <T> ApiResponse<T> executeRequest(Function<String, T> parser, Supplier<HttpClient.HttpResult> requestSupplier) {
        try {
            HttpClient.HttpResult result = requestSupplier.get();
            T data = parser.apply(result.getBody());
            return ApiResponse.success(result.getStatusCode(), data);
        } catch (IpsApiException e) {
            return ApiResponse.failure(e.getStatusCode(), e.getMessage(), e.getResponseBody());
        } catch (IpsNetworkException e) {
            return ApiResponse.failure(0, "网络错误: " + e.getMessage(), null);
        } catch (IpsSdkException e) {
            return ApiResponse.failure(-1, "SDK错误: " + e.getMessage(), null);
        }
    }
    
    // ========== 身份证检测 ==========
    
    /**
     * 身份证预处理 (v1 - Base64)
     */
    public ApiResponse<List<IdCardResult>> idCardPreprocess(IdCardRequest request) {
        return executeRequest(
            body -> httpClient.parseListResponse(body, IdCardResult.class),
            () -> httpClient.postJson("/api/v1/idcard/preprocess", request)
        );
    }
    
    /**
     * 身份证预处理 (v3 - 文件上传)
     */
    public ApiResponse<List<IdCardResult>> idCardPreprocessV3(InputStream inputStream, String fileName, IdCardUploadParams params) {
        return executeUpload(inputStream, fileName, "/api/v3/idcard/preprocess", params.toMap(),
            body -> httpClient.parseListResponse(body, IdCardResult.class));
    }
    
    /**
     * 身份证预处理 (v3 - 简化版，使用File)
     */
    public ApiResponse<List<IdCardResult>> idCardPreprocessV3(File file, IdCardUploadParams params) {
        return withFileInputStream(file, (fis, name) -> idCardPreprocessV3(fis, name, params));
    }
    
    // ========== 营业执照检测 ==========
    
    /**
     * 营业执照预处理 (v1 - Base64)
     */
    public ApiResponse<List<BizLicResult>> bizLicPreprocess(BizLicRequest request) {
        return executeRequest(
            body -> httpClient.parseListResponse(body, BizLicResult.class),
            () -> httpClient.postJson("/api/v1/bizlic/preprocess", request)
        );
    }
    
    /**
     * 营业执照预处理 (v3 - 文件上传)
     */
    public ApiResponse<List<BizLicResult>> bizLicPreprocessV3(InputStream inputStream, String fileName, BizLicUploadParams params) {
        return executeUpload(inputStream, fileName, "/api/v3/bizlic/preprocess", params.toMap(),
            body -> httpClient.parseListResponse(body, BizLicResult.class));
    }
    
    /**
     * 营业执照预处理 (v3 - 简化版，使用File)
     */
    public ApiResponse<List<BizLicResult>> bizLicPreprocessV3(File file, BizLicUploadParams params) {
        return withFileInputStream(file, (fis, name) -> bizLicPreprocessV3(fis, name, params));
    }
    
    // ========== 印章检测 ==========
    
    /**
     * 印章预处理 (v1 - Base64)
     */
    public ApiResponse<List<SealResult>> sealPreprocess(SealRequest request) {
        return executeRequest(
            body -> httpClient.parseListResponse(body, SealResult.class),
            () -> httpClient.postJson("/api/v1/seal/preprocess", request)
        );
    }
    
    /**
     * 印章识别 v2 (支持PDF)
     */
    public ApiResponse<List<SealResult>> sealPreprocessV2(SealRequest request) {
        return executeRequest(
            body -> httpClient.parseListResponse(body, SealResult.class),
            () -> httpClient.postJson("/api/v2/seal/preprocess", request)
        );
    }
    
    /**
     * 印章检测 v3 (文件上传)
     */
    public ApiResponse<List<SealResult>> sealPreprocessV3(InputStream inputStream, String fileName, SealUploadParams params) {
        return executeUpload(inputStream, fileName, "/api/v3/seal/preprocess", params.toMap(),
            body -> httpClient.parseListResponse(body, SealResult.class));
    }
    
    /**
     * 印章检测 v3 (简化版，使用File)
     */
    public ApiResponse<List<SealResult>> sealPreprocessV3(File file, SealUploadParams params) {
        return withFileInputStream(file, (fis, name) -> sealPreprocessV3(fis, name, params));
    }
    
    // ========== 通用证件检测 ==========
    
    /**
     * 通用证件检测 (v2 - Base64)
     */
    public ApiResponse<CardResult> cardDetection(CardRequest request) {
        return executeRequest(
            body -> httpClient.parseResponse(body, CardResult.class),
            () -> httpClient.postJson("/api/v2/card/det", request)
        );
    }
    
    /**
     * 通用证件检测 (v3 - 文件上传)
     */
    public ApiResponse<CardResult> cardDetectionV3(InputStream inputStream, String fileName, boolean returnCorpImage) {
        Map<String, Object> params = new HashMap<>();
        params.put("return_corp_image", returnCorpImage);
        return executeUpload(inputStream, fileName, "/api/v3/card/det", params,
            body -> httpClient.parseResponse(body, CardResult.class));
    }
    
    /**
     * 通用证件检测 (v3 - 简化版，使用File)
     */
    public ApiResponse<CardResult> cardDetectionV3(File file, boolean returnCorpImage) {
        return withFileInputStream(file, (fis, name) -> cardDetectionV3(fis, name, returnCorpImage));
    }
    
    // ========== OCR 识别 ==========
    
    /**
     * 通用OCR识别 v2 (支持PDF)
     */
    public ApiResponse<List<OcrResult>> ocrPreprocessV2(OcrRequest request) {
        return executeRequest(
            body -> httpClient.parseListResponse(body, OcrResult.class),
            () -> httpClient.postJson("/api/v2/ocr/preprocess", request)
        );
    }
    
    /**
     * OCR识别 v3 (文件上传)
     */
    public ApiResponse<OcrResult> ocrPreprocessV3(InputStream inputStream, String fileName, OcrUploadParams params) {
        return executeUpload(inputStream, fileName, "/api/v3/ocr/preprocess", params.toMap(),
            body -> httpClient.parseResponse(body, OcrResult.class));
    }
    
    /**
     * OCR识别 v3 (简化版，使用File)
     */
    public ApiResponse<OcrResult> ocrPreprocessV3(File file, OcrUploadParams params) {
        return withFileInputStream(file, (fis, name) -> ocrPreprocessV3(fis, name, params));
    }
    
    // ========== 发票识别 ==========
    
    /**
     * 发票识别 v2 (支持PDF)
     */
    public ApiResponse<List<InvoiceResult>> invoicePreprocessV2(InvoiceRequest request) {
        return executeRequest(
            body -> httpClient.parseListResponse(body, InvoiceResult.class),
            () -> httpClient.postJson("/api/v2/invoice/preprocess", request)
        );
    }
    
    /**
     * 发票识别 v3 (文件上传)
     */
    public ApiResponse<List<InvoiceResult>> invoicePreprocessV3(InputStream inputStream, String fileName, InvoiceUploadParams params) {
        return executeUpload(inputStream, fileName, "/api/v3/invoice/preprocess", params.toMap(),
            body -> httpClient.parseListResponse(body, InvoiceResult.class));
    }
    
    /**
     * 发票识别 v3 (简化版，使用File)
     */
    public ApiResponse<List<InvoiceResult>> invoicePreprocessV3(File file, InvoiceUploadParams params) {
        return withFileInputStream(file, (fis, name) -> invoicePreprocessV3(fis, name, params));
    }
    
    // ========== 手印检测 ==========
    
    /**
     * 手印检测 v2 (文件上传)
     */
    public ApiResponse<HandprintResult> handprintDetectV2(InputStream inputStream, String fileName, String position) {
        Map<String, Object> params = new HashMap<>();
        if (position != null) {
            params.put("position", position);
        }
        return executeUpload(inputStream, fileName, "/api/v2/handprint/detect", params,
            body -> httpClient.parseResponse(body, HandprintResult.class));
    }
    
    /**
     * 手印检测 v2 (简化版，使用File)
     */
    public ApiResponse<HandprintResult> handprintDetectV2(File file, String position) {
        return withFileInputStream(file, (fis, name) -> handprintDetectV2(fis, name, position));
    }
    
    // ========== 健康检查 ==========
    
    /**
     * 健康检查
     */
    public ApiResponse<HealthStatus> healthCheck() {
        return executeRequest(
            body -> httpClient.parseResponse(body, HealthStatus.class),
            () -> httpClient.get("/health")
        );
    }
    
    // ========== 辅助方法 ==========
    
    /**
     * 执行文件上传请求
     */
    private <T> ApiResponse<T> executeUpload(InputStream inputStream, String fileName, String path, 
                                            Map<String, Object> params, Function<String, T> parser) {
        try {
            String mimeType = HttpClient.getMimeType(fileName);
            HttpClient.HttpResult result = httpClient.uploadStream(path, inputStream, fileName, mimeType, params);
            T data = parser.apply(result.getBody());
            return ApiResponse.success(result.getStatusCode(), data);
        } catch (IpsApiException e) {
            return ApiResponse.failure(e.getStatusCode(), e.getMessage(), e.getResponseBody());
        } catch (IpsNetworkException e) {
            return ApiResponse.failure(0, "网络错误: " + e.getMessage(), null);
        } catch (IpsSdkException e) {
            return ApiResponse.failure(-1, "SDK错误: " + e.getMessage(), null);
        }
    }
    
    /**
     * 使用FileInputStream执行操作，自动管理资源
     */
    private <T> ApiResponse<T> withFileInputStream(File file, FileInputStreamFunction<T> function) {
        try (FileInputStream fis = new FileInputStream(file)) {
            return function.apply(fis, file.getName());
        } catch (IOException e) {
            return ApiResponse.failure(-1, "文件读取失败: " + e.getMessage(), null);
        }
    }
    
    @FunctionalInterface
    private interface FileInputStreamFunction<T> {
        ApiResponse<T> apply(InputStream inputStream, String fileName) throws IOException;
    }
    
    // ========== 关闭客户端 ==========
    
    public void close() {
        httpClient.close();
    }
}
