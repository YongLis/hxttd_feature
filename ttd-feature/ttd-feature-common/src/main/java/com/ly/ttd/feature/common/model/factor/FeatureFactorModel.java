package com.ly.ttd.feature.common.model.factor;

import lombok.Data;

/**
 * 实时速率指标
 *
 * @author yong.li
 * @since 2026/5/23 21:06
 */
@Data
public class FeatureFactorModel extends FactorModel {
    /**
     * 关联速率编码
     */
    private String refVelocityCode;
}
