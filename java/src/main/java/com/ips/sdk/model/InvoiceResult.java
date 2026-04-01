package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发票识别结果
 */
@Data
@NoArgsConstructor
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
}
