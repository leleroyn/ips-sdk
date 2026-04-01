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
    
    // ========== 身份证检测 ==========
    
    /**
     * 身份证预处理 (v1 - Base64)
     */
    public ApiResponse<List<IdCardResult>> idCardPreprocess(IdCardRequest request) {
        try {
            HttpClient.HttpResult result = httpClient.postJson("/api/v1/idcard/preprocess", request);
            List<IdCardResult> data = httpClient.parseListResponse(result.getBody(), IdCardResult.class);
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
     * 身份证预处理 (v3 - 文件上传)
     * @param inputStream 文件输入流
     * @param fileName 文件名（带扩展名，如 "idcard.jpg"）
     * @param params 上传参数
     */
    public ApiResponse<List<IdCardResult>> idCardPreprocessV3(InputStream inputStream, String fileName, IdCardUploadParams params) {
        try {
            String mimeType = HttpClient.getMimeType(fileName);
            HttpClient.HttpResult result = httpClient.uploadStream("/api/v3/idcard/preprocess", inputStream, fileName, mimeType, params.toMap());
            List<IdCardResult> data = httpClient.parseListResponse(result.getBody(), IdCardResult.class);
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
     * 身份证预处理 (v3 - 简化版，使用File)
     */
    public ApiResponse<List<IdCardResult>> idCardPreprocessV3(File file, IdCardUploadParams params) {
        try (FileInputStream fis = new FileInputStream(file)) {
            return idCardPreprocessV3(fis, file.getName(), params);
        } catch (IOException e) {
            return ApiResponse.failure(-1, "文件读取失败: " + e.getMessage(), null);
        }
    }
    
    // ========== 营业执照检测 ==========
    
    /**
     * 营业执照预处理 (v1 - Base64)
     */
    public ApiResponse<List<BizLicResult>> bizLicPreprocess(BizLicRequest request) {
        try {
            HttpClient.HttpResult result = httpClient.postJson("/api/v1/bizlic/preprocess", request);
            List<BizLicResult> data = httpClient.parseListResponse(result.getBody(), BizLicResult.class);
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
     * 营业执照预处理 (v3 - 文件上传)
     */
    public ApiResponse<List<BizLicResult>> bizLicPreprocessV3(InputStream inputStream, String fileName, BizLicUploadParams params) {
        try {
            String mimeType = HttpClient.getMimeType(fileName);
            HttpClient.HttpResult result = httpClient.uploadStream("/api/v3/bizlic/preprocess", inputStream, fileName, mimeType, params.toMap());
            List<BizLicResult> data = httpClient.parseListResponse(result.getBody(), BizLicResult.class);
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
     * 营业执照预处理 (v3 - 简化版，使用File)
     */
    public ApiResponse<List<BizLicResult>> bizLicPreprocessV3(File file, BizLicUploadParams params) {
        try (FileInputStream fis = new FileInputStream(file)) {
            return bizLicPreprocessV3(fis, file.getName(), params);
        } catch (IOException e) {
            return ApiResponse.failure(-1, "文件读取失败: " + e.getMessage(), null);
        }
    }
    
    // ========== 印章检测 ==========
    
    /**
     * 印章预处理 (v1 - Base64)
     */
    public ApiResponse<List<SealResult>> sealPreprocess(SealRequest request) {
        try {
            HttpClient.HttpResult result = httpClient.postJson("/api/v1/seal/preprocess", request);
            List<SealResult> data = httpClient.parseListResponse(result.getBody(), SealResult.class);
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
     * 印章识别 v2 (支持PDF)
     */
    public ApiResponse<List<SealResult>> sealPreprocessV2(SealRequest request) {
        try {
            HttpClient.HttpResult result = httpClient.postJson("/api/v2/seal/preprocess", request);
            List<SealResult> data = httpClient.parseListResponse(result.getBody(), SealResult.class);
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
     * 印章检测 v3 (文件上传)
     */
    public ApiResponse<List<SealResult>> sealPreprocessV3(InputStream inputStream, String fileName, SealUploadParams params) {
        try {
            String mimeType = HttpClient.getMimeType(fileName);
            HttpClient.HttpResult result = httpClient.uploadStream("/api/v3/seal/preprocess", inputStream, fileName, mimeType, params.toMap());
            List<SealResult> data = httpClient.parseListResponse(result.getBody(), SealResult.class);
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
     * 印章检测 v3 (简化版，使用File)
     */
    public ApiResponse<List<SealResult>> sealPreprocessV3(File file, SealUploadParams params) {
        try (FileInputStream fis = new FileInputStream(file)) {
            return sealPreprocessV3(fis, file.getName(), params);
        } catch (IOException e) {
            return ApiResponse.failure(-1, "文件读取失败: " + e.getMessage(), null);
        }
    }
    
    // ========== 通用证件检测 ==========
    
    /**
     * 通用证件检测 (v2 - Base64)
     */
    public ApiResponse<CardResult> cardDetection(CardRequest request) {
        try {
            HttpClient.HttpResult result = httpClient.postJson("/api/v2/card/det", request);
            CardResult data = httpClient.parseResponse(result.getBody(), CardResult.class);
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
     * 通用证件检测 (v3 - 文件上传)
     */
    public ApiResponse<CardResult> cardDetectionV3(InputStream inputStream, String fileName, boolean returnCorpImage) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("return_corp_image", returnCorpImage);
            String mimeType = HttpClient.getMimeType(fileName);
            HttpClient.HttpResult result = httpClient.uploadStream("/api/v3/card/det", inputStream, fileName, mimeType, params);
            CardResult data = httpClient.parseResponse(result.getBody(), CardResult.class);
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
     * 通用证件检测 (v3 - 简化版，使用File)
     */
    public ApiResponse<CardResult> cardDetectionV3(File file, boolean returnCorpImage) {
        try (FileInputStream fis = new FileInputStream(file)) {
            return cardDetectionV3(fis, file.getName(), returnCorpImage);
        } catch (IOException e) {
            return ApiResponse.failure(-1, "文件读取失败: " + e.getMessage(), null);
        }
    }
    
    // ========== OCR 识别 ==========
    
    /**
     * 通用OCR识别 v2 (支持PDF)
     */
    public ApiResponse<List<OcrResult>> ocrPreprocessV2(OcrRequest request) {
        try {
            HttpClient.HttpResult result = httpClient.postJson("/api/v2/ocr/preprocess", request);
            List<OcrResult> data = httpClient.parseListResponse(result.getBody(), OcrResult.class);
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
     * OCR识别 v3 (文件上传)
     */
    public ApiResponse<OcrResult> ocrPreprocessV3(InputStream inputStream, String fileName, OcrUploadParams params) {
        try {
            String mimeType = HttpClient.getMimeType(fileName);
            HttpClient.HttpResult result = httpClient.uploadStream("/api/v3/ocr/preprocess", inputStream, fileName, mimeType, params.toMap());
            OcrResult data = httpClient.parseResponse(result.getBody(), OcrResult.class);
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
     * OCR识别 v3 (简化版，使用File)
     */
    public ApiResponse<OcrResult> ocrPreprocessV3(File file, OcrUploadParams params) {
        try (FileInputStream fis = new FileInputStream(file)) {
            return ocrPreprocessV3(fis, file.getName(), params);
        } catch (IOException e) {
            return ApiResponse.failure(-1, "文件读取失败: " + e.getMessage(), null);
        }
    }
    
    // ========== 发票识别 ==========
    
    /**
     * 发票识别 v2 (支持PDF)
     */
    public ApiResponse<List<InvoiceResult>> invoicePreprocessV2(InvoiceRequest request) {
        try {
            HttpClient.HttpResult result = httpClient.postJson("/api/v2/invoice/preprocess", request);
            List<InvoiceResult> data = httpClient.parseListResponse(result.getBody(), InvoiceResult.class);
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
     * 发票识别 v3 (文件上传)
     */
    public ApiResponse<List<InvoiceResult>> invoicePreprocessV3(InputStream inputStream, String fileName, InvoiceUploadParams params) {
        try {
            String mimeType = HttpClient.getMimeType(fileName);
            HttpClient.HttpResult result = httpClient.uploadStream("/api/v3/invoice/preprocess", inputStream, fileName, mimeType, params.toMap());
            List<InvoiceResult> data = httpClient.parseListResponse(result.getBody(), InvoiceResult.class);
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
     * 发票识别 v3 (简化版，使用File)
     */
    public ApiResponse<List<InvoiceResult>> invoicePreprocessV3(File file, InvoiceUploadParams params) {
        try (FileInputStream fis = new FileInputStream(file)) {
            return invoicePreprocessV3(fis, file.getName(), params);
        } catch (IOException e) {
            return ApiResponse.failure(-1, "文件读取失败: " + e.getMessage(), null);
        }
    }
    
    // ========== 手印检测 ==========
    
    /**
     * 手印检测 v2 (文件上传)
     */
    public ApiResponse<HandprintResult> handprintDetectV2(InputStream inputStream, String fileName, String position) {
        try {
            Map<String, Object> params = new HashMap<>();
            if (position != null) {
                params.put("position", position);
            }
            String mimeType = HttpClient.getMimeType(fileName);
            HttpClient.HttpResult result = httpClient.uploadStream("/api/v2/handprint/detect", inputStream, fileName, mimeType, params);
            HandprintResult data = httpClient.parseResponse(result.getBody(), HandprintResult.class);
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
     * 手印检测 v2 (简化版，使用File)
     */
    public ApiResponse<HandprintResult> handprintDetectV2(File file, String position) {
        try (FileInputStream fis = new FileInputStream(file)) {
            return handprintDetectV2(fis, file.getName(), position);
        } catch (IOException e) {
            return ApiResponse.failure(-1, "文件读取失败: " + e.getMessage(), null);
        }
    }
    
    // ========== 健康检查 ==========
    
    /**
     * 健康检查
     */
    public ApiResponse<HealthStatus> healthCheck() {
        try {
            HttpClient.HttpResult result = httpClient.get("/health");
            HealthStatus data = httpClient.parseResponse(result.getBody(), HealthStatus.class);
            return ApiResponse.success(result.getStatusCode(), data);
        } catch (IpsApiException e) {
            return ApiResponse.failure(e.getStatusCode(), e.getMessage(), e.getResponseBody());
        } catch (IpsNetworkException e) {
            return ApiResponse.failure(0, "网络错误: " + e.getMessage(), null);
        } catch (IpsSdkException e) {
            return ApiResponse.failure(-1, "SDK错误: " + e.getMessage(), null);
        }
    }
    
    // ========== 关闭客户端 ==========
    
    public void close() {
        httpClient.close();
    }
}
