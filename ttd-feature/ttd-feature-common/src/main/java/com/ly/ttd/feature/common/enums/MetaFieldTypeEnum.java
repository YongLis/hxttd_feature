package com.ly.ttd.feature.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字段类型 1-基本数据 2-衍生数据
 */
@Getter
@AllArgsConstructor
public enum MetaFieldTypeEnum {
    /**
     * 字段类型 1-基本数据 2-衍生数据
     */
    BASIC_DATA(1, "基本数据"),
    DERIVED_DATA(2, "衍生数据");
    private final Integer code;
    private final String desc;

    public static MetaFieldTypeEnum getEnumByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (MetaFieldTypeEnum enumType : values()) {
            if (enumType.code.equals(code)) {
                return enumType;
            }
        }
        return null;
    }
}
