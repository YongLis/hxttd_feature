package com.ly.ttd.feature.request;

/**
 * 实时特征查询
 *
 * @author yong.li
 * @since 2026/3/19 16:22
 */

import lombok.Data;

@Data
public class TxnFeatureQueryRequest {
    /**
     * 交易ID
     */

    private String txnId;
    /**
     * 实时特征编码
     */
    private String velocityCode;
    /**
     * 交易类型
     */
    private String masterValue;
}
