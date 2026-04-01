package com.ips.sdk.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ips.sdk.config.IpsConfig;
import com.ips.sdk.exception.IpsApiException;
import com.ips.sdk.exception.IpsNetworkException;
import com.ips.sdk.exception.IpsSdkException;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * HTTP 客户端
 */
public class HttpClient {
    
    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType STREAM_MEDIA_TYPE = MediaType.parse("application/octet-stream");
    
    /**
     * MIME类型映射表
     */
    private static final Map<String, String> MIME_TYPE_MAP = new HashMap<>();
    static {
        MIME_TYPE_MAP.put(".jpg", "image/jpeg");
        MIME_TYPE_MAP.put(".jpeg", "image/jpeg");
        MIME_TYPE_MAP.put(".png", "image/png");
        MIME_TYPE_MAP.put(".gif", "image/gif");
        MIME_TYPE_MAP.put(".pdf", "application/pdf");
        MIME_TYPE_MAP.put(".bmp", "image/bmp");
        MIME_TYPE_MAP.put(".webp", "image/webp");
    }
    
    private final String baseUrl;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    
    /**
     * HTTP 结果包装类
     */
    public static class HttpResult {
        private final int statusCode;
        private final String body;
        
        public HttpResult(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }
        
        public int getStatusCode() {
            return statusCode;
        }
        
        public String getBody() {
            return body;
        }
    }
    
    public HttpClient(IpsConfig config) {
        this.baseUrl = config.getBaseUrl();
        
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(config.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(config.getWriteTimeout(), TimeUnit.MILLISECONDS)
                .build();
        
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    /**
     * POST JSON请求
     */
    public HttpResult postJson(String path, Object request) throws IpsSdkException {
        String url = baseUrl + path;
        
        try {
            String jsonBody = objectMapper.writeValueAsString(request);
            RequestBody body = RequestBody.create(jsonBody, JSON_MEDIA_TYPE);
            
            Request httpRequest = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            
            logger.debug("POST {}", url);
            
            try (Response response = okHttpClient.newCall(httpRequest).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                
                if (!response.isSuccessful()) {
                    logger.error("API请求失败: {} - {} - {}", url, response.code(), responseBody);
                    throw new IpsApiException(response.code(), "API请求失败: " + response.message(), responseBody);
                }
                
                return new HttpResult(response.code(), responseBody);
            }
        } catch (IOException e) {
            logger.error("网络请求异常: {}", url, e);
            throw new IpsNetworkException("网络请求失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * GET请求
     */
    public HttpResult get(String path) throws IpsSdkException {
        String url = baseUrl + path;
        
        try {
            Request httpRequest = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            
            logger.debug("GET {}", url);
            
            try (Response response = okHttpClient.newCall(httpRequest).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                
                if (!response.isSuccessful()) {
                    logger.error("API请求失败: {} - {} - {}", url, response.code(), responseBody);
                    throw new IpsApiException(response.code(), "API请求失败: " + response.message(), responseBody);
                }
                
                return new HttpResult(response.code(), responseBody);
            }
        } catch (IOException e) {
            logger.error("网络请求异常: {}", url, e);
            throw new IpsNetworkException("网络请求失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 上传文件流
     * @param path API路径
     * @param inputStream 文件输入流
     * @param fileName 文件名（带扩展名）
     * @param mimeType MIME类型（如 "image/jpeg", "application/pdf"）
     * @param params 表单参数
     */
    public HttpResult uploadStream(String path, InputStream inputStream, String fileName, String mimeType, Map<String, Object> params) throws IpsSdkException {
        String url = baseUrl + path;
        
        try {
            // 读取输入流到字节数组
            byte[] bytes = toByteArray(inputStream);
            
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", fileName, RequestBody.create(bytes, MediaType.parse(mimeType)));
            
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (entry.getValue() != null) {
                        builder.addFormDataPart(entry.getKey(), String.valueOf(entry.getValue()));
                    }
                }
            }
            
            RequestBody requestBody = builder.build();
            
            Request httpRequest = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            
            logger.debug("UPLOAD {} - file: {}", url, fileName);
            
            try (Response response = okHttpClient.newCall(httpRequest).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                
                if (!response.isSuccessful()) {
                    logger.error("文件上传失败: {} - {} - {}", url, response.code(), responseBody);
                    throw new IpsApiException(response.code(), "文件上传失败: " + response.message(), responseBody);
                }
                
                return new HttpResult(response.code(), responseBody);
            }
        } catch (IOException e) {
            logger.error("文件上传异常: {}", url, e);
            throw new IpsNetworkException("文件上传失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * InputStream转byte数组
     */
    private byte[] toByteArray(InputStream inputStream) throws IOException {
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[8192];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }
    
    /**
     * 根据文件扩展名获取MIME类型
     */
    public static String getMimeType(String fileName) {
        if (fileName == null) {
            return "application/octet-stream";
        }
        String lowerName = fileName.toLowerCase();
        for (Map.Entry<String, String> entry : MIME_TYPE_MAP.entrySet()) {
            if (lowerName.endsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "application/octet-stream";
    }
    
    /**
     * 解析响应
     */
    public <T> T parseResponse(String json, Class<T> responseClass) throws IpsSdkException {
        try {
            return objectMapper.readValue(json, responseClass);
        } catch (IOException e) {
            logger.error("JSON解析失败: {}", json, e);
            throw new IpsSdkException("JSON解析失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 解析列表响应
     */
    public <T> List<T> parseListResponse(String json, Class<T> elementClass) throws IpsSdkException {
        try {
            return objectMapper.readValue(json, new TypeReference<List<T>>() {});
        } catch (IOException e) {
            logger.error("JSON列表解析失败: {}", json, e);
            throw new IpsSdkException("JSON列表解析失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 关闭客户端
     */
    public void close() {
        okHttpClient.dispatcher().executorService().shutdown();
        okHttpClient.connectionPool().evictAll();
    }
}
