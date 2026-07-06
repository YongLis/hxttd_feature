package com.ly.ttd.feature.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yong.li
 * @since 2026/5/20 19:21
 */
@Getter
@AllArgsConstructor
public enum VelocityExpireUnitEnum {
    SECOND("SECOND", "秒", 1),
    MINUTE("MINUTE", "分", 60),
    HOUR("HOUR", "时", 60 * 60),
    DAY("DAY", "天", 24 * 60 * 60),
    MONTH("MONTH", "月", 30 * 24 * 3600),
    YEAR("YEAR", "年", 365 * 24 * 3600),
    ;
    private final String type;
    private final String desc;
    private final Integer seconds;


    public static String getByType(String type) {
        for (VelocityExpireUnitEnum valueTypeEnum : VelocityExpireUnitEnum.values()) {
            if (valueTypeEnum.getType().equals(type)) {
                return valueTypeEnum.getDesc();
            }
        }
        return null;
    }

    public static String getTypeByDesc(String desc) {
        for (VelocityExpireUnitEnum valueTypeEnum : VelocityExpireUnitEnum.values()) {
            if (valueTypeEnum.getDesc().equals(desc)) {
                return valueTypeEnum.getType();
            }
        }
        return null;
    }


    public static Integer getSecondByType(String type) {
        for (VelocityExpireUnitEnum valueTypeEnum : VelocityExpireUnitEnum.values()) {
            if (valueTypeEnum.getType().equals(type)) {
                return valueTypeEnum.getSeconds();
            }
        }
        return 0;
    }
}
