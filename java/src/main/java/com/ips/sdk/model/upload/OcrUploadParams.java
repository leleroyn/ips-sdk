package com.ips.sdk.model.upload;

import lombok.*;
import java.util.HashMap;
import java.util.Map;

/**
 * OCR上传参数 (v3版本)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcrUploadParams {
    
    private String appId;
    
    @Builder.Default
    private Boolean resize = true;
    
    @Builder.Default
    private Boolean useCls = false;
    
    @Builder.Default
    private String format = "txt";
    
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (appId != null) map.put("app_id", appId);
        map.put("resize", resize);
        map.put("use_cls", useCls);
        map.put("format", format);
        return map;
    }
}
