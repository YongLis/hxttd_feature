package com.ly.ttd.feature.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * schema状态
 *
 * @apiNote 1/冻结 2/正常
 */
@Getter
@AllArgsConstructor
public enum MetaSchemaStatusEnum {
    /**
     * 状态 1-正常(上线) 2-冻结(下线)
     */
    ONLINE(1, "正常"),
    OFFLINE(2, "冻结");
    private final Integer code;
    private final String desc;

    public static MetaSchemaStatusEnum getEnumByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (MetaSchemaStatusEnum enumType : values()) {
            if (enumType.code.equals(code)) {
                return enumType;
            }
        }
        return null;
    }
}
