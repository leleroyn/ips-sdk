package com.ips.sdk.model;

/**
 * API 响应包装类
 * 统一处理成功和失败的响应
 * 
 * @param <T> 响应数据类型
 */
public class ApiResponse<T> {
    
    /**
     * 是否成功
     */
    private final boolean success;
    
    /**
     * HTTP 状态码
     */
    private final int statusCode;
    
    /**
     * 响应数据
     */
    private final T data;
    
    /**
     * 错误消息
     */
    private final String errorMessage;
    
    /**
     * 原始响应体
     */
    private final String rawResponseBody;
    
    private ApiResponse(boolean success, int statusCode, T data, String errorMessage, String rawResponseBody) {
        this.success = success;
        this.statusCode = statusCode;
        this.data = data;
        this.errorMessage = errorMessage;
        this.rawResponseBody = rawResponseBody;
    }
    
    /**
     * 创建成功响应
     */
    public static <T> ApiResponse<T> success(int statusCode, T data) {
        return new ApiResponse<>(true, statusCode, data, null, null);
    }
    
    /**
     * 创建失败响应
     */
    public static <T> ApiResponse<T> failure(int statusCode, String errorMessage, String rawResponseBody) {
        return new ApiResponse<>(false, statusCode, null, errorMessage, rawResponseBody);
    }
    
    // Getters
    public boolean isSuccess() {
        return success;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public T getData() {
        return data;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public String getRawResponseBody() {
        return rawResponseBody;
    }
    
    /**
     * 是否为客户端错误 (4xx)
     */
    public boolean isClientError() {
        return statusCode >= 400 && statusCode < 500;
    }
    
    /**
     * 是否为服务器错误 (5xx)
     */
    public boolean isServerError() {
        return statusCode >= 500 && statusCode < 600;
    }
    
    @Override
    public String toString() {
        if (success) {
            return "ApiResponse{success=true, statusCode=" + statusCode + ", data=" + data + "}";
        } else {
            return "ApiResponse{success=false, statusCode=" + statusCode + 
                   ", error='" + errorMessage + "'}";
        }
    }
}
