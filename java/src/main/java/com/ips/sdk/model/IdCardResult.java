package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 身份证检测结果
 */
@Data
@NoArgsConstructor
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
}
