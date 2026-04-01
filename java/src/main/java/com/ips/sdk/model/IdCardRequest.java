package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 身份证检测请求参数
 */
public class IdCardRequest {
    
    /**
     * 图像Base64编码
     */
    @JsonProperty("image_base64")
    private String imageBase64;
    
    /**
     * 是否返回裁剪图像
     */
    @JsonProperty("return_corp_image")
    private Boolean returnCorpImage;
    
    /**
     * 是否返回OCR文本
     */
    @JsonProperty("return_ocr_text")
    private Boolean returnOcrText;
    
    /**
     * 工具配置
     */
    @JsonProperty("tool")
    private ToolConfig tool;
    
    // 构造方法
    public IdCardRequest() {
    }
    
    public IdCardRequest(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
    
    // Getters and Setters
    public String getImageBase64() {
        return imageBase64;
    }
    
    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
    
    public Boolean getReturnCorpImage() {
        return returnCorpImage;
    }
    
    public void setReturnCorpImage(Boolean returnCorpImage) {
        this.returnCorpImage = returnCorpImage;
    }
    
    public Boolean getReturnOcrText() {
        return returnOcrText;
    }
    
    public void setReturnOcrText(Boolean returnOcrText) {
        this.returnOcrText = returnOcrText;
    }
    
    public ToolConfig getTool() {
        return tool;
    }
    
    public void setTool(ToolConfig tool) {
        this.tool = tool;
    }
    
    // Builder方法
    public IdCardRequest withReturnCorpImage(boolean returnCorpImage) {
        this.returnCorpImage = returnCorpImage;
        return this;
    }
    
    public IdCardRequest withReturnOcrText(boolean returnOcrText) {
        this.returnOcrText = returnOcrText;
        return this;
    }
    
    public IdCardRequest withTool(ToolConfig tool) {
        this.tool = tool;
        return this;
    }
}
