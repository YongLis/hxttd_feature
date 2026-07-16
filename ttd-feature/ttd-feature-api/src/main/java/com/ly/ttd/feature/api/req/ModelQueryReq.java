package com.ly.ttd.feature.api.req;

import lombok.Data;

/**
 * 模型查询
 *
 * @author yong.li
 * @since 2026/6/12 14:40
 */
@Data
public class ModelQueryReq extends TxnFeatureReq {
    /**
     * 按模型查询
     */
    private String modelCode;
}
