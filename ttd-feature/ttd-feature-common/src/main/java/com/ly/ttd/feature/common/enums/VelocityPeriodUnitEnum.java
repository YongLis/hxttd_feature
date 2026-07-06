package com.ly.ttd.feature.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 实时特征周期单位
 *
 * @author yong.li
 * @since 2026/3/28 17:46
 */
@Getter
@AllArgsConstructor
public enum VelocityPeriodUnitEnum {
    YEAR("YEAR", "年", "Y"),
    MONTH("MONTH", "月", "M"),
    DAY("DAY", "日", "D"),
    HOUR("HOUR", "时", "H"),
    MINUTE("MINUTE", "分", "MIN");
    private final String code;
    private final String name;
    private final String shortCode;


    public static String getByCode(String code) {
        for (VelocityPeriodUnitEnum factorGenerateTypeEnum : VelocityPeriodUnitEnum.values()) {
            if (factorGenerateTypeEnum.getCode().equals(code)) {
                return factorGenerateTypeEnum.getName();
            }
        }
        return null;
    }

    public static String getShoreCodeByCode(String code) {
        for (VelocityPeriodUnitEnum factorGenerateTypeEnum : VelocityPeriodUnitEnum.values()) {
            if (factorGenerateTypeEnum.getCode().equals(code)) {
                return factorGenerateTypeEnum.getShortCode();
            }
        }
        return null;
    }
}
