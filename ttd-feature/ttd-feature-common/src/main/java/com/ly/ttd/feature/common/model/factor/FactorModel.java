package com.ly.ttd.feature.common.model.factor;

import com.ly.ttd.feature.common.model.BaseResourceModel;
import lombok.Data;

/**
 * 指标
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class FactorModel extends BaseResourceModel {
    /**
     * 指标类型(META/DERIVATIVE/FEATURE)
     */
    private String factorType;

}
