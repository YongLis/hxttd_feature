package com.ly.ttd.feature.common.model.factor;

import lombok.Data;

/**
 * 元字段指标
 *
 * @author yong.li
 * @since 2026/5/23 21:06
 */
@Data
public class MetaFactorModel extends FactorModel {

    /**
     * 关联元字段
     */
    private String metaFieldCode;


}
