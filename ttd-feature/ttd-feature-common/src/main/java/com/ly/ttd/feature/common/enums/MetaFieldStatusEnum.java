package com.ly.ttd.feature.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态 1-正常(上线) 2-冻结(下线)
 */
@Getter
@AllArgsConstructor
public enum MetaFieldStatusEnum {
    /**
     * 状态 1-正常(上线) 2-冻结(下线)
     */
    ONLINE(1, "正常"),
    OFFLINE(2, "冻结");
    private final Integer code;
    private final String desc;

    public static MetaFieldStatusEnum getEnumByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (MetaFieldStatusEnum enumType : values()) {
            if (enumType.code.equals(code)) {
                return enumType;
            }
        }
        return null;
    }
}
