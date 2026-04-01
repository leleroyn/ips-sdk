package com.ips.sdk.config;

/**
 * IPS SDK 配置类
 */
public class IpsConfig {
    
    /**
     * 默认基础URL
     */
    public static final String DEFAULT_BASE_URL = "http://localhost:8000";
    
    /**
     * 默认连接超时时间 (毫秒)
     */
    public static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    
    /**
     * 默认读取超时时间 (毫秒)
     */
    public static final int DEFAULT_READ_TIMEOUT = 30000;
    
    /**
     * 默认写入超时时间 (毫秒)
     */
    public static final int DEFAULT_WRITE_TIMEOUT = 30000;
    
    private String baseUrl = DEFAULT_BASE_URL;
    private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    private int readTimeout = DEFAULT_READ_TIMEOUT;
    private int writeTimeout = DEFAULT_WRITE_TIMEOUT;
    
    public IpsConfig() {
    }
    
    public IpsConfig(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public IpsConfig setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }
    
    public int getConnectTimeout() {
        return connectTimeout;
    }
    
    public IpsConfig setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }
    
    public int getReadTimeout() {
        return readTimeout;
    }
    
    public IpsConfig setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }
    
    public int getWriteTimeout() {
        return writeTimeout;
    }
    
    public IpsConfig setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }
}
