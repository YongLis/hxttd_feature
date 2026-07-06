package com.ly.ttd.feature.common.enums;

/**
 * @author yong.li
 * @since 2026/1/27 11:08
 */
public enum ExecuteStateEnum {
    SUCCESS("SUCCESS", "成功"),
    FAILED("FAILED", "失败"),
    ERROR("ERROR", "错误");

    private String code;
    private String message;

    private ExecuteStateEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
