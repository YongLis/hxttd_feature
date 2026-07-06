package com.ly.ttd.biz.admin.consts;

import org.apache.commons.lang3.StringUtils;

/**
 * @author yong.li
 * @since 2026/4/29 22:13
 */
public enum StatusEnum {
    WAIT("0", "待发布"),
    PUBLISH("1", "发布"),
    OFFLINE("2", "下线"),
    ;


    private String code;
    private String desc;

    StatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (StatusEnum statusEnum : values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum.getDesc();
            }
        }
        return null;
    }
}
