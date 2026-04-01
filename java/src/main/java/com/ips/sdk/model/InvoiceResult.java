package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 发票识别结果
 */
public class InvoiceResult {
    
    @JsonProperty("corp_image_base64")
    private String corpImageBase64;
    
    @JsonProperty("confidence")
    private Double confidence;
    
    /**
     * 发票类型
     */
    @JsonProperty("invoice_type")
    private Integer invoiceType;
    
    @JsonProperty("ocr_text")
    private String ocrText;
    
    @JsonProperty("source_text")
    private String sourceText;
    
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
    
    public Integer getInvoiceType() {
        return invoiceType;
    }
    
    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }
    
    public String getOcrText() {
        return ocrText;
    }
    
    public void setOcrText(String ocrText) {
        this.ocrText = ocrText;
    }
    
    public String getSourceText() {
        return sourceText;
    }
    
    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }
    
    @Override
    public String toString() {
        return "InvoiceResult{" +
                "invoiceType=" + invoiceType +
                ", confidence=" + confidence +
                ", hasCorpImage=" + (corpImageBase64 != null) +
                ", hasOcrText=" + (ocrText != null) +
                '}';
    }
}
