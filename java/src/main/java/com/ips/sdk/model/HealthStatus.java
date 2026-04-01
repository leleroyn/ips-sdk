package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 健康检查状态
 */
public class HealthStatus {
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("timestamp")
    private Long timestamp;
    
    // Getters and Setters
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "HealthStatus{" +
                "status='" + status + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
