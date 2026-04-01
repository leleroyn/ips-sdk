package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * OCR识别请求参数
 */
public class OcrRequest {
    
    @JsonProperty("image_base64")
    private String imageBase64;
    
    /**
     * 文件类型 (例如: "image", "pdf")
     */
    @JsonProperty("file_type")
    private String fileType;
    
    @JsonProperty("return_image")
    private Boolean returnImage;
    
    @JsonProperty("return_text")
    private Boolean returnText;
    
    public OcrRequest() {
    }
    
    public OcrRequest(String imageBase64) {
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
    
    public Boolean getReturnImage() {
        return returnImage;
    }
    
    public void setReturnImage(Boolean returnImage) {
        this.returnImage = returnImage;
    }
    
    public Boolean getReturnText() {
        return returnText;
    }
    
    public void setReturnText(Boolean returnText) {
        this.returnText = returnText;
    }
    
    public OcrRequest withFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }
    
    public OcrRequest withReturnImage(boolean returnImage) {
        this.returnImage = returnImage;
        return this;
    }
    
    public OcrRequest withReturnText(boolean returnText) {
        this.returnText = returnText;
        return this;
    }
}
