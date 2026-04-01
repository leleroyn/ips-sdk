package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 印章检测结果
 */
@Data
@NoArgsConstructor
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
}
