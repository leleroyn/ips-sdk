package com.ips.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 手印检测结果
 */
@Data
public class HandprintResult {
    
    @JsonProperty("has_handprint")
    private Boolean hasHandprint;

}
