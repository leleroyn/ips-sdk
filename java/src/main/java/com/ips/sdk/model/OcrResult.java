package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OCR识别结果
 */
@Data
@NoArgsConstructor
public class OcrResult {
    
    /**
     * OCR内容
     */
    @JsonProperty("ocr_text")
    private String ocrText;
}
