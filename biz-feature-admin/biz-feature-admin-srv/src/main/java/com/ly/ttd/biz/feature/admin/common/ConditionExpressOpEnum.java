package com.ly.ttd.biz.feature.admin.common;

/**
 * 条件操作符
 *
 * @author yong.li
 * @since 2026/4/13 14:33
 */
public enum ConditionExpressOpEnum {
    // 等于 equals ， 包含 contain, 不等于 not_equals, 在范围内in, 大于等于 greater_than, 小于等于 less_than
    EQUALS("equals", "等于"),
    NOT_EQUALS("not_equals", "不等于"),
    CONTAIN("contain", "包含"),
    GREATER_THAN("greater_than", "大于"),
    GREATER_THAN_OR_EQUAL("greater_than_or_equal", "大于等于"),
    LESS_THAN("less_than", "小于"),
    LESS_THAN_OR_EQUAL("less_than_or_equal", "小于等于"),
    IN("in", "在范围内"),
    NOT_IN("not_in", "不在范围内"),
    ;


    private String code;
    private String msg;

    ConditionExpressOpEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 根据code获取状态枚举
     */
    public static String getStatusMsg(String code) {
        for (ConditionExpressOpEnum statusEnum : ConditionExpressOpEnum.values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum.getMsg();
            }
        }
        return null;
    }

}
