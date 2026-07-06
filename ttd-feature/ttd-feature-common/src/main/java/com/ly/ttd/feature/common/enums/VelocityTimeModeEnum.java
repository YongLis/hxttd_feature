package com.ly.ttd.feature.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yong.li
 * @since 2026/5/20 19:19
 */
@Getter
@AllArgsConstructor
public enum VelocityTimeModeEnum {
    TTL("TTL", "过期时间"),
    CURRENT_DAY("DAY", "自然日"),
    CURRENT_MONTH("MONTH", "自然月"),
    CURRENT_YEAR("YEAR", "自然年"),
    FOREVER("FOREVER", "永久累计");
    private final String type;
    private final String desc;

    public static String getByType(String type) {
        for (VelocityTimeModeEnum valueTypeEnum : VelocityTimeModeEnum.values()) {
            if (valueTypeEnum.getType().equals(type)) {
                return valueTypeEnum.getDesc();
            }
        }
        return null;
    }

    public static String getByDesc(String desc) {
        for (VelocityTimeModeEnum valueTypeEnum : VelocityTimeModeEnum.values()) {
            if (valueTypeEnum.getDesc().equals(desc)) {
                return valueTypeEnum.getType();
            }
        }
        return null;
    }

    //根据type获取枚举
    public static VelocityTimeModeEnum getEnumByType(String type) {
        for (VelocityTimeModeEnum valueTypeEnum : VelocityTimeModeEnum.values()) {
            if (valueTypeEnum.getType().equals(type)) {
                return valueTypeEnum;
            }
        }
        return null;
    }

}
