package com.ips.sdk.exception;

/**
 * API调用异常
 */
public class IpsApiException extends IpsSdkException {
    
    private static final long serialVersionUID = 1L;
    
    private final int statusCode;
    private final String responseBody;
    
    public IpsApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = null;
    }
    
    public IpsApiException(int statusCode, String message, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public String getResponseBody() {
        return responseBody;
    }
    
    @Override
    public String toString() {
        return "IpsApiException{" +
                "statusCode=" + statusCode +
                ", message='" + getMessage() + '\'' +
                ", responseBody='" + responseBody + '\'' +
                '}';
    }
}
