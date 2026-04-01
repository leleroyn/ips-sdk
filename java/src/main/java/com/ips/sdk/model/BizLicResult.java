package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 营业执照检测结果
 */
public class BizLicResult {
    
    @JsonProperty("corp_image_base64")
    private String corpImageBase64;
    
    @JsonProperty("confidence")
    private Double confidence;
    
    /**
     * 营业执照类型
     */
    @JsonProperty("bizlic_type")
    private Integer bizlicType;
    
    @JsonProperty("ocr_text")
    private String ocrText;
    
    // Getters and Setters
    public String getCorpImageBase64() {
        return corpImageBase64;
    }
    
    public void setCorpImageBase64(String corpImageBase64) {
        this.corpImageBase64 = corpImageBase64;
    }
    
    public Double getConfidence() {
        return confidence;
    }
    
    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
    
    public Integer getBizlicType() {
        return bizlicType;
    }
    
    public void setBizlicType(Integer bizlicType) {
        this.bizlicType = bizlicType;
    }
    
    public String getOcrText() {
        return ocrText;
    }
    
    public void setOcrText(String ocrText) {
        this.ocrText = ocrText;
    }
    
    @Override
    public String toString() {
        return "BizLicResult{" +
                "bizlicType=" + bizlicType +
                ", confidence=" + confidence +
                ", hasCorpImage=" + (corpImageBase64 != null) +
                ", hasOcrText=" + (ocrText != null) +
                '}';
    }
}
