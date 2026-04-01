package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用证件检测结果
 */
@Data
@NoArgsConstructor
public class CardResult {
    
    @JsonProperty("corp_image_base64")
    private String corpImageBase64;
    
    @JsonProperty("card_info")
    private CardInfo cardInfo;
    
    @JsonProperty("fork")
    private Boolean fork;
}
