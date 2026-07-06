package com.ly.ttd.feature.srv.data.dto;

import lombok.Data;

/**
 * @author yong.li
 * @since 2026/7/4 21:49
 */
@Data
public class FeatureMiddleData {

    private String uniqueKey;

    private String value;

    private Long expireTime;


    public FeatureMiddleData(String uniqueKey, String value, Long expireTime) {
        this.uniqueKey = uniqueKey;
        this.value = value;
        this.expireTime = expireTime;
    }
}
