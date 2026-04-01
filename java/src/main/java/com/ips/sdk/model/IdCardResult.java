package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 身份证检测结果
 */
public class IdCardResult {
    
    /**
     * 裁剪后的身份证图像 (Base64编码)
     */
    @JsonProperty("corp_image_base64")
    private String corpImageBase64;
    
    /**
     * 置信度
     */
    @JsonProperty("confidence")
    private Double confidence;
    
    /**
     * 身份证类型
     * 0: 正面
     * 1: 背面
     */
    @JsonProperty("idcard_type")
    private Integer idcardType;
    
    /**
     * OCR识别文本
     */
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
    
    public Integer getIdcardType() {
        return idcardType;
    }
    
    public void setIdcardType(Integer idcardType) {
        this.idcardType = idcardType;
    }
    
    public String getOcrText() {
        return ocrText;
    }
    
    public void setOcrText(String ocrText) {
        this.ocrText = ocrText;
    }
    
    @Override
    public String toString() {
        return "IdCardResult{" +
                "idcardType=" + idcardType +
                ", confidence=" + confidence +
                ", hasCorpImage=" + (corpImageBase64 != null) +
                ", hasOcrText=" + (ocrText != null) +
                '}';
    }
}
