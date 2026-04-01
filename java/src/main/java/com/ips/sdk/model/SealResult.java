package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 印章检测结果
 */
public class SealResult {
    
    @JsonProperty("seal_image_base64")
    private String sealImageBase64;
    
    @JsonProperty("confidence")
    private Double confidence;
    
    /**
     * 印章类型
     */
    @JsonProperty("seal_type")
    private Integer sealType;
    
    @JsonProperty("ocr_result")
    private String ocrResult;
    
    // Getters and Setters
    public String getSealImageBase64() {
        return sealImageBase64;
    }
    
    public void setSealImageBase64(String sealImageBase64) {
        this.sealImageBase64 = sealImageBase64;
    }
    
    public Double getConfidence() {
        return confidence;
    }
    
    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
    
    public Integer getSealType() {
        return sealType;
    }
    
    public void setSealType(Integer sealType) {
        this.sealType = sealType;
    }
    
    public String getOcrResult() {
        return ocrResult;
    }
    
    public void setOcrResult(String ocrResult) {
        this.ocrResult = ocrResult;
    }
    
    @Override
    public String toString() {
        return "SealResult{" +
                "sealType=" + sealType +
                ", confidence=" + confidence +
                ", hasSealImage=" + (sealImageBase64 != null) +
                ", hasOcrResult=" + (ocrResult != null) +
                '}';
    }
}
