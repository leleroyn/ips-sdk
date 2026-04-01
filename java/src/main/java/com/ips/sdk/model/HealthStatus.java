package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 健康检查状态
 */
@Data
@NoArgsConstructor
public class HealthStatus {
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("timestamp")
    private Long timestamp;
}
