package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 印章检测请求参数
 */
public class SealRequest {
    
    @JsonProperty("image_base64")
    private String imageBase64;
    
    /**
     * 文件类型 (例如: "image", "pdf")
     */
    @JsonProperty("file_type")
    private String fileType;
    
    @JsonProperty("return_seal_image")
    private Boolean returnSealImage;
    
    @JsonProperty("return_ocr_text")
    private Boolean returnOcrText;
    
    @JsonProperty("tool")
    private ToolConfig tool;
    
    public SealRequest() {
    }
    
    public SealRequest(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
    
    // Getters and Setters
    public String getImageBase64() {
        return imageBase64;
    }
    
    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
    
    public String getFileType() {
        return fileType;
    }
    
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    public Boolean getReturnSealImage() {
        return returnSealImage;
    }
    
    public void setReturnSealImage(Boolean returnSealImage) {
        this.returnSealImage = returnSealImage;
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
    
    public SealRequest withFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }
    
    public SealRequest withReturnSealImage(boolean returnSealImage) {
        this.returnSealImage = returnSealImage;
        return this;
    }
    
    public SealRequest withReturnOcrText(boolean returnOcrText) {
        this.returnOcrText = returnOcrText;
        return this;
    }
    
    public SealRequest withTool(ToolConfig tool) {
        this.tool = tool;
        return this;
    }
}
