package com.ips.sdk.model.upload;

import java.util.HashMap;
import java.util.Map;

/**
 * 发票上传参数 (v3版本)
 */
public class InvoiceUploadParams {
    
    private Boolean returnCorpImage = true;
    private Boolean returnOcrText = false;
    private Double initConfidence = 0.5;
    private Boolean resize = true;
    private Boolean backGround = false;
    
    public InvoiceUploadParams() {
    }
    
    // Getters and Setters
    public Boolean getReturnCorpImage() {
        return returnCorpImage;
    }
    
    public void setReturnCorpImage(Boolean returnCorpImage) {
        this.returnCorpImage = returnCorpImage;
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
    public InvoiceUploadParams withReturnCorpImage(boolean returnCorpImage) {
        this.returnCorpImage = returnCorpImage;
        return this;
    }
    
    public InvoiceUploadParams withReturnOcrText(boolean returnOcrText) {
        this.returnOcrText = returnOcrText;
        return this;
    }
    
    public InvoiceUploadParams withInitConfidence(double initConfidence) {
        this.initConfidence = initConfidence;
        return this;
    }
    
    public InvoiceUploadParams withResize(boolean resize) {
        this.resize = resize;
        return this;
    }
    
    public InvoiceUploadParams withBackGround(boolean backGround) {
        this.backGround = backGround;
        return this;
    }
    
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("return_corp_image", returnCorpImage);
        map.put("return_ocr_text", returnOcrText);
        map.put("init_confidence", initConfidence);
        map.put("resize", resize);
        map.put("back_ground", backGround);
        return map;
    }
}
