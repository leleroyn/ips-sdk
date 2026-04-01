package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 工具配置
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToolConfig {
    
    /**
     * 初始置信度阈值
     */
    @JsonProperty("init_confidence")
    private Double initConfidence;
    
    /**
     * 是否调整图像大小
     */
    @JsonProperty("resize")
    private Boolean resize;
    
    /**
     * 是否添加背景
     */
    @JsonProperty("back_ground")
    private Boolean backGround;
}
