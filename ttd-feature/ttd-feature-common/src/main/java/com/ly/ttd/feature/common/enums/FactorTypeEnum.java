package com.ly.ttd.feature.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yong.li
 * @since 2026/1/29 14:45
 */
@Getter
@AllArgsConstructor
public enum FactorTypeEnum {
    META("META", "元数据指标"),
    DERIVATIVE("DERIVATIVE", "衍生指标"),
    FEATURE("FEATURE", "实时特征指标");


    private String code;
    private String desc;
}
