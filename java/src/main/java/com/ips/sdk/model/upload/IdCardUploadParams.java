package com.ips.sdk.model.upload;

import lombok.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份证上传参数 (v3版本)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdCardUploadParams {
    
    @Builder.Default
    private Boolean returnCorpImage = true;
    
    @Builder.Default
    private Boolean returnOcrText = false;
    
    @Builder.Default
    private Double initConfidence = 0.5;
    
    @Builder.Default
    private Boolean resize = true;
    
    @Builder.Default
    private Boolean backGround = false;
    
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
