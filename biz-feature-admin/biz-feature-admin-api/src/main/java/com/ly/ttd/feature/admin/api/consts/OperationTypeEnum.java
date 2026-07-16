package com.ly.ttd.feature.admin.api.consts;

import org.apache.commons.lang3.StringUtils;

/**
 * @author yong.li
 * @since 2026/4/29 22:13
 */
public enum OperationTypeEnum {
    ADD("ADD", "新增"),
    UPDATE("UPDATE", "更新"),
    DELETE("DELETE", "删除"),
    PUBLISH("PUBLISH", "发布"),
    OFFLINE("OFFLINE", "下线"),
    ROLL_BACK("ROLL_BACK", "回滚"),
    ;


    private String code;
    private String desc;

    OperationTypeEnum(String code, String desc) {
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
        for (OperationTypeEnum statusEnum : values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum.getDesc();
            }
        }
        return null;
    }
}
