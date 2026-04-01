package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OCR识别请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcrRequest {
    
    @JsonProperty("image_base64")
    private String imageBase64;
    
    /**
     * 文件类型 (例如: "image", "pdf")
     */
    @JsonProperty("file_type")
    private String fileType;
    
    @JsonProperty("return_image")
    private Boolean returnImage;
    
    @JsonProperty("return_text")
    private Boolean returnText;
}
