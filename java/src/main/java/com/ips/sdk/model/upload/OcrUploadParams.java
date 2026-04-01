package com.ips.sdk.model.upload;

import java.util.HashMap;
import java.util.Map;

/**
 * OCR上传参数 (v3版本)
 */
public class OcrUploadParams {
    
    private String appId;
    private Boolean resize = true;
    private Boolean useCls = false;
    private String format = "txt";
    
    public OcrUploadParams() {
    }
    
    // Getters and Setters
    public String getAppId() {
        return appId;
    }
    
    public void setAppId(String appId) {
        this.appId = appId;
    }
    
    public Boolean getResize() {
        return resize;
    }
    
    public void setResize(Boolean resize) {
        this.resize = resize;
    }
    
    public Boolean getUseCls() {
        return useCls;
    }
    
    public void setUseCls(Boolean useCls) {
        this.useCls = useCls;
    }
    
    public String getFormat() {
        return format;
    }
    
    public void setFormat(String format) {
        this.format = format;
    }
    
    // Builder方法
    public OcrUploadParams withAppId(String appId) {
        this.appId = appId;
        return this;
    }
    
    public OcrUploadParams withResize(boolean resize) {
        this.resize = resize;
        return this;
    }
    
    public OcrUploadParams withUseCls(boolean useCls) {
        this.useCls = useCls;
        return this;
    }
    
    public OcrUploadParams withFormat(String format) {
        this.format = format;
        return this;
    }
    
    /**
     * 转换为Map
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (appId != null) map.put("app_id", appId);
        map.put("resize", resize);
        map.put("use_cls", useCls);
        map.put("format", format);
        return map;
    }
}
