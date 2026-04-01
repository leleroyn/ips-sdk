package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 证件信息
 */
@Data
@NoArgsConstructor
public class CardInfo {
    
    /**
     * 证件类型
     */
    @JsonProperty("card_type")
    private Integer cardType;
    
    @JsonProperty("confidence")
    private Double confidence;
}
