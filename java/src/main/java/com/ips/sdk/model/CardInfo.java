package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 证件信息
 */
public class CardInfo {
    
    /**
     * 证件类型
     */
    @JsonProperty("card_type")
    private Integer cardType;
    
    @JsonProperty("confidence")
    private Double confidence;
    
    // Getters and Setters
    public Integer getCardType() {
        return cardType;
    }
    
    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }
    
    public Double getConfidence() {
        return confidence;
    }
    
    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
    
    @Override
    public String toString() {
        return "CardInfo{" +
                "cardType=" + cardType +
                ", confidence=" + confidence +
                '}';
    }
}
