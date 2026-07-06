package com.ly.ttd.feature.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 实时特征计算方式
 *
 * @author yong.li
 * @since 2026/3/28 17:46
 */
@Getter
@AllArgsConstructor
public enum VelocityCaluateTypeEnum {
    SUM("SUM", "求和"),
    COUNT("COUNT", "计数"),
    AVG("AVG", "平均值"),
    MIN("MIN", "最小值"),
    MAX("MAX", "最大值");
    private final String code;
    private final String name;


    public static String getByCode(String code) {
        for (VelocityCaluateTypeEnum factorGenerateTypeEnum : VelocityCaluateTypeEnum.values()) {
            if (factorGenerateTypeEnum.getCode().equals(code)) {
                return factorGenerateTypeEnum.getName();
            }
        }
        return null;
    }

    public static String getCodeByName(String name) {
        for (VelocityCaluateTypeEnum factorGenerateTypeEnum : VelocityCaluateTypeEnum.values()) {
            if (factorGenerateTypeEnum.getName().equals(name)) {
                return factorGenerateTypeEnum.getCode();
            }
        }
        return null;
    }
}
