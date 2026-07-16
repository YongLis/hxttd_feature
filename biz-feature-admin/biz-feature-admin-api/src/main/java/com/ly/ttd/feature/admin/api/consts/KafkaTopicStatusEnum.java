package com.ly.ttd.feature.admin.api.consts;

import org.apache.commons.lang3.StringUtils;

/**
 * 初始化INIT,审核中AUDIT，AUDIT_PASS, AUDIT_REJECT
 *
 * @author yong.li
 * @since 2026/4/29 22:13
 */
public enum KafkaTopicStatusEnum {
    INIT("INIT", "初始化"),
    AUDIT("AUDIT", "审核中"),
    AUDIT_PASS("AUDIT_PASS", "审核通过"),
    AUDIT_REJECT("AUDIT_REJECT", "审核拒绝");


    private String code;
    private String desc;

    KafkaTopicStatusEnum(String code, String desc) {
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
        for (KafkaTopicStatusEnum statusEnum : values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum.getDesc();
            }
        }
        return null;
    }
}
