package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 工具配置
 */
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
    
    // 构造方法
    public ToolConfig() {
    }
    
    public ToolConfig(Double initConfidence, Boolean resize, Boolean backGround) {
        this.initConfidence = initConfidence;
        this.resize = resize;
        this.backGround = backGround;
    }
    
    // Getters and Setters
    public Double getInitConfidence() {
        return initConfidence;
    }
    
    public void setInitConfidence(Double initConfidence) {
        this.initConfidence = initConfidence;
    }
    
    public Boolean getResize() {
        return resize;
    }
    
    public void setResize(Boolean resize) {
        this.resize = resize;
    }
    
    public Boolean getBackGround() {
        return backGround;
    }
    
    public void setBackGround(Boolean backGround) {
        this.backGround = backGround;
    }
    
    // Builder方法
    public ToolConfig withInitConfidence(double initConfidence) {
        this.initConfidence = initConfidence;
        return this;
    }
    
    public ToolConfig withResize(boolean resize) {
        this.resize = resize;
        return this;
    }
    
    public ToolConfig withBackGround(boolean backGround) {
        this.backGround = backGround;
        return this;
    }
}
