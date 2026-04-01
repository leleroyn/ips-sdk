package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 印章检测请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SealRequest {
    
    @JsonProperty("image_base64")
    private String imageBase64;
    
    /**
     * 文件类型 (例如: "image", "pdf")
     */
    @JsonProperty("file_type")
    private String fileType;
    
    @JsonProperty("return_seal_image")
    private Boolean returnSealImage;
    
    @JsonProperty("return_ocr_text")
    private Boolean returnOcrText;
    
    @JsonProperty("tool")
    private ToolConfig tool;
}
