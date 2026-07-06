package com.ly.ttd.feature.common.event.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yong.li
 * @since 2026/5/20 16:46
 */
@Data
public class VelValueItem {
    /**
     * 交易号
     */
    private String txnId;

    /**
     * 时间戳
     */
    private Long ts;
    /**
     * 实时特征值
     */
    private BigDecimal v;


    public VelValueItem(String txnId, Long ts, BigDecimal v) {
        this.txnId = txnId;
        this.ts = ts;
        this.v = v;
    }
}
