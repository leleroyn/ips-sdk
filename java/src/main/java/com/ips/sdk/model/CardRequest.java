package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 通用证件检测请求参数
 */
public class CardRequest {
    
    @JsonProperty("image_base64")
    private String imageBase64;
    
    @JsonProperty("return_corp_image")
    private Boolean returnCorpImage;
    
    public CardRequest() {
    }
    
    public CardRequest(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
    
    // Getters and Setters
    public String getImageBase64() {
        return imageBase64;
    }
    
    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
    
    public Boolean getReturnCorpImage() {
        return returnCorpImage;
    }
    
    public void setReturnCorpImage(Boolean returnCorpImage) {
        this.returnCorpImage = returnCorpImage;
    }
    
    public CardRequest withReturnCorpImage(boolean returnCorpImage) {
        this.returnCorpImage = returnCorpImage;
        return this;
    }
}
