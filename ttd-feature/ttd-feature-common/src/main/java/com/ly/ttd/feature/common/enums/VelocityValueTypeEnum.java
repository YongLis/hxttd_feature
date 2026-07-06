package com.ly.ttd.feature.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 实时特征值类型
 *
 * @author yong.li
 * @since 2026/3/28 17:46
 */
@Getter
@AllArgsConstructor
public enum VelocityValueTypeEnum {
    FIX_VALUE("FIX", "固定值"),
    DYNAMIC_VALUE("DYNAMIC", "动态值"),

    ;

    private final String code;
    private final String name;

    public static String getByCode(String code) {
        for (VelocityValueTypeEnum factorGenerateTypeEnum : VelocityValueTypeEnum.values()) {
            if (factorGenerateTypeEnum.getCode().equals(code)) {
                return factorGenerateTypeEnum.getName();
            }
        }
        return null;
    }
}
