package com.ly.ttd.feature.common.event.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 速率计算结果
 *
 * @author yong.li
 * @since 2026/4/24 15:21
 */
@Data
public class VelCalculateResult {

    /**
     * 速率计算结果
     */
    private BigDecimal result;
    /**
     * 计算时样本快照数据
     */
    private String sampleSnapshot;

    public VelCalculateResult(BigDecimal result, String sampleSnapshot) {
        this.result = result;
        this.sampleSnapshot = sampleSnapshot;
    }
}
