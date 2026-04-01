package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 手印检测结果
 */
@Data
public class HandprintResult {
    
    /**
     * 是否存在手印
     */
    @JsonProperty("has_handprint")
    private Boolean hasHandprint;
}
