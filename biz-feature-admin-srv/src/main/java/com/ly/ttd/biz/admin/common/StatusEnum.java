package com.ly.ttd.biz.admin.common;

/**
 * @author yong.li
 * @since 2026/4/13 14:33
 */
public enum StatusEnum {
    Draft("0", "草稿"),
    Publish("1", "发布"),
    Offline("2", "下线");


    private String code;
    private String msg;

    StatusEnum(String code, String msg) {
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
        for (StatusEnum statusEnum : StatusEnum.values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum.getMsg();
            }
        }
        return null;
    }

}
