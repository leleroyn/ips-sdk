package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用证件检测请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardRequest {
    
    @JsonProperty("image_base64")
    private String imageBase64;
    
    @JsonProperty("return_corp_image")
    private Boolean returnCorpImage;
}
