package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 营业执照检测结果
 */
@Data
@NoArgsConstructor
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
}
