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
    
    @JsonProperty("image_base64")
    private String imageBase64;
    
    @JsonProperty("text")
    private String text;
}
