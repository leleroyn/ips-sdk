package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * OCR识别结果
 */
public class OcrResult {
    
    @JsonProperty("image_base64")
    private String imageBase64;
    
    @JsonProperty("text")
    private String text;
    
    // Getters and Setters
    public String getImageBase64() {
        return imageBase64;
    }
    
    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return "OcrResult{" +
                "hasImage=" + (imageBase64 != null) +
                ", hasText=" + (text != null) +
                '}';
    }
}
