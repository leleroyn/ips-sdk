package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 身份证检测请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdCardRequest {
    
    /**
     * 图像Base64编码
     */
    @JsonProperty("image_base64")
    private String imageBase64;
    
    /**
     * 是否返回裁剪图像
     */
    @JsonProperty("return_corp_image")
    private Boolean returnCorpImage;
    
    /**
     * 是否返回OCR文本
     */
    @JsonProperty("return_ocr_text")
    private Boolean returnOcrText;
    
    /**
     * 工具配置
     */
    @JsonProperty("tool")
    private ToolConfig tool;
}
