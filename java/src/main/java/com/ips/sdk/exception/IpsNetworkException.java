package com.ips.sdk.exception;

/**
 * 网络异常
 */
public class IpsNetworkException extends IpsSdkException {
    
    private static final long serialVersionUID = 1L;
    
    public IpsNetworkException(String message) {
        super(message);
    }
    
    public IpsNetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
