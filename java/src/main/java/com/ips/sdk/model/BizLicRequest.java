package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 营业执照检测请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BizLicRequest {
    
    @JsonProperty("image_base64")
    private String imageBase64;
    
    @JsonProperty("return_corp_image")
    private Boolean returnCorpImage;
    
    @JsonProperty("return_ocr_text")
    private Boolean returnOcrText;
    
    @JsonProperty("tool")
    private ToolConfig tool;
}
