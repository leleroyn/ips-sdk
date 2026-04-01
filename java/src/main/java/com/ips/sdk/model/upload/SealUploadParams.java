package com.ips.sdk.model.upload;

import java.util.HashMap;
import java.util.Map;

/**
 * 印章上传参数 (v3版本)
 */
public class SealUploadParams {
    
    private Boolean returnSealImage = true;
    private Boolean returnOcrText = false;
    private Double initConfidence = 0.5;
    private Boolean resize = true;
    private Boolean backGround = true;
    
    public SealUploadParams() {
    }
    
    // Getters and Setters
    public Boolean getReturnSealImage() {
        return returnSealImage;
    }
    
    public void setReturnSealImage(Boolean returnSealImage) {
        this.returnSealImage = returnSealImage;
    }
    
    public Boolean getReturnOcrText() {
        return returnOcrText;
    }
    
    public void setReturnOcrText(Boolean returnOcrText) {
        this.returnOcrText = returnOcrText;
    }
    
    public Double getInitConfidence() {
        return initConfidence;
    }
    
    public void setInitConfidence(Double initConfidence) {
        this.initConfidence = initConfidence;
    }
    
    public Boolean getResize() {
        return resize;
    }
    
    public void setResize(Boolean resize) {
        this.resize = resize;
    }
    
    public Boolean getBackGround() {
        return backGround;
    }
    
    public void setBackGround(Boolean backGround) {
        this.backGround = backGround;
    }
    
    // Builder方法
    public SealUploadParams withReturnSealImage(boolean returnSealImage) {
        this.returnSealImage = returnSealImage;
        return this;
    }
    
    public SealUploadParams withReturnOcrText(boolean returnOcrText) {
        this.returnOcrText = returnOcrText;
        return this;
    }
    
    public SealUploadParams withInitConfidence(double initConfidence) {
        this.initConfidence = initConfidence;
        return this;
    }
    
    public SealUploadParams withResize(boolean resize) {
        this.resize = resize;
        return this;
    }
    
    public SealUploadParams withBackGround(boolean backGround) {
        this.backGround = backGround;
        return this;
    }
    
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("return_seal_image", returnSealImage);
        map.put("return_ocr_text", returnOcrText);
        map.put("init_confidence", initConfidence);
        map.put("resize", resize);
        map.put("back_ground", backGround);
        return map;
    }
}
