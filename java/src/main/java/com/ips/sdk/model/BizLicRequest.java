package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 营业执照检测请求参数
 */
public class BizLicRequest {
    
    @JsonProperty("image_base64")
    private String imageBase64;
    
    @JsonProperty("return_corp_image")
    private Boolean returnCorpImage;
    
    @JsonProperty("return_ocr_text")
    private Boolean returnOcrText;
    
    @JsonProperty("tool")
    private ToolConfig tool;
    
    public BizLicRequest() {
    }
    
    public BizLicRequest(String imageBase64) {
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
    
    public BizLicRequest withReturnCorpImage(boolean returnCorpImage) {
        this.returnCorpImage = returnCorpImage;
        return this;
    }
    
    public BizLicRequest withReturnOcrText(boolean returnOcrText) {
        this.returnOcrText = returnOcrText;
        return this;
    }
    
    public BizLicRequest withTool(ToolConfig tool) {
        this.tool = tool;
        return this;
    }
}
