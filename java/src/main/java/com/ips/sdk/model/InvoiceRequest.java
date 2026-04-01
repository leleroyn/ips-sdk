package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 发票识别请求参数
 */
public class InvoiceRequest {
    
    @JsonProperty("image_base64")
    private String imageBase64;
    
    /**
     * 文件类型 (例如: "image", "pdf")
     */
    @JsonProperty("file_type")
    private String fileType;
    
    @JsonProperty("return_corp_image")
    private Boolean returnCorpImage;
    
    @JsonProperty("return_ocr_text")
    private Boolean returnOcrText;
    
    @JsonProperty("tool")
    private ToolConfig tool;
    
    public InvoiceRequest() {
    }
    
    public InvoiceRequest(String imageBase64) {
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
    
    public InvoiceRequest withFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }
    
    public InvoiceRequest withReturnCorpImage(boolean returnCorpImage) {
        this.returnCorpImage = returnCorpImage;
        return this;
    }
    
    public InvoiceRequest withReturnOcrText(boolean returnOcrText) {
        this.returnOcrText = returnOcrText;
        return this;
    }
    
    public InvoiceRequest withTool(ToolConfig tool) {
        this.tool = tool;
        return this;
    }
}
