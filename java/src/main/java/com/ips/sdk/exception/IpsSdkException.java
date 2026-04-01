package com.ips.sdk.exception;

/**
 * IPS SDK 异常基类
 */
public class IpsSdkException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public IpsSdkException(String message) {
        super(message);
    }
    
    public IpsSdkException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public IpsSdkException(Throwable cause) {
        super(cause);
    }
}
