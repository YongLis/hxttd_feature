package com.ly.ttd.feature.common.enums;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 返回对象类型
 *
 * @author yong.li
 * @since 2026/1/29 15:27
 */
public enum ObjectTypeEnum {
    LIST("LIST", "列表", "[]"),
    LONG("LONG", "整数", "0"),
    DOUBLE("DOUBLE", "小数", "0.0"),
    DECIMAL("DECIMAL", "金额", "0.0"),
    DATE("DATE", "日期", "2099-01-01 00:00:00"),
    DICT("DICT", "字典", "{}"),
    STRING("STRING", "字符串", "-999999"),
    BOOLEAN("BOOLEAN", "布尔值", "false");
    private String code;
    private String desc;
    private String defaultValue;

    private ObjectTypeEnum(String code, String desc, String defaultValue) {
        this.code = code;
        this.desc = desc;
        this.defaultValue = defaultValue;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * 根据code获取desc
     */
    public static String getDescByCode(String code) {
        for (ObjectTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return null;
    }

    /**
     * 根据Code获取枚举
     */
    public static ObjectTypeEnum getByCode(String code) {
        for (ObjectTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 根据Code获取默认值
     */
    public static String getDefaultValueByCode(String code) {
        for (ObjectTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type.getDefaultValue();
            }
        }
        return null;
    }

    /**
     * 根据类型获取Class
     */
    public static Class<?> getClassByTypeCode(String typeCode) {
        ObjectTypeEnum type = getByCode(typeCode);
        switch (type) {
            case LIST:
                return List.class;
            case LONG:
                return Long.class;
            case DOUBLE:
                return Double.class;
            case DECIMAL:
                return BigDecimal.class;
            case DATE:
                return LocalDateTime.class;
            case DICT:
                return Map.class;
            case STRING:
                return String.class;
            case BOOLEAN:
                return Boolean.class;
            default:
                return null;
        }

    }


}
