package com.ly.ttd.feature.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 因子类型
 *
 * @author yong.li
 * @since 2026/3/28 17:46
 */
@Getter
@AllArgsConstructor
public enum FactorGenerateTypeEnum {
    NATIVE("native", "基础因子"),
    DERIVATIVE("derivative", "衍生因子"),
    VELOCITY("velocity", "实时特征因子");

    private final String code;
    private final String name;

    public static String getByCode(String code) {
        for (FactorGenerateTypeEnum factorGenerateTypeEnum : FactorGenerateTypeEnum.values()) {
            if (factorGenerateTypeEnum.getCode().equals(code)) {
                return factorGenerateTypeEnum.getName();
            }
        }
        return null;
    }
}
