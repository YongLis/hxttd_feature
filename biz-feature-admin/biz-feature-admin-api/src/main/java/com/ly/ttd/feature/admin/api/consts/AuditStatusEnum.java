package com.ly.ttd.feature.admin.api.consts;

import org.apache.commons.lang3.StringUtils;

/**
 * @author yong.li
 * @since 2026/4/29 22:13
 */
public enum AuditStatusEnum {
    PENDING("PENDING", "待审核"),
    APPROVED("APPROVED", "审核通过"),
    REJECTED("REJECTED", "审核拒绝"),
    ;


    private String code;
    private String desc;

    AuditStatusEnum(String code, String desc) {
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
        for (AuditStatusEnum statusEnum : values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum.getDesc();
            }
        }
        return null;
    }
}
