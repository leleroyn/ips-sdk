package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 通用证件检测结果
 */
public class CardResult {
    
    @JsonProperty("corp_image_base64")
    private String corpImageBase64;
    
    @JsonProperty("card_info")
    private CardInfo cardInfo;
    
    @JsonProperty("fork")
    private Boolean fork;
    
    // Getters and Setters
    public String getCorpImageBase64() {
        return corpImageBase64;
    }
    
    public void setCorpImageBase64(String corpImageBase64) {
        this.corpImageBase64 = corpImageBase64;
    }
    
    public CardInfo getCardInfo() {
        return cardInfo;
    }
    
    public void setCardInfo(CardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }
    
    public Boolean getFork() {
        return fork;
    }
    
    public void setFork(Boolean fork) {
        this.fork = fork;
    }
    
    @Override
    public String toString() {
        return "CardResult{" +
                "cardInfo=" + cardInfo +
                ", fork=" + fork +
                ", hasCorpImage=" + (corpImageBase64 != null) +
                '}';
    }
}
