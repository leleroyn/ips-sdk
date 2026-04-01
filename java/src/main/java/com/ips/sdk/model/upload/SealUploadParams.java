package com.ips.sdk.model.upload;

import lombok.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 印章上传参数 (v3版本)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SealUploadParams {
    
    @Builder.Default
    private Boolean returnSealImage = true;
    
    @Builder.Default
    private Boolean returnOcrText = false;
    
    @Builder.Default
    private Double initConfidence = 0.5;
    
    @Builder.Default
    private Boolean resize = true;
    
    @Builder.Default
    private Boolean backGround = true;
    
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
