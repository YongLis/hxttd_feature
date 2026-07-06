package com.ly.ttd.feature.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 调用状态类型
 *
 * @author yong.li
 * @since 2026/3/28 17:46
 */
@Getter
@AllArgsConstructor
public enum CallStateEnum {
    SUCCESS("success", "成功"),
    FAIL("fail", "失败"),
    TIMEOUT("timeout", "超时"),
    CANCEL("cancel", "取消");

    private final String code;
    private final String name;

    public static String getByCode(String code) {
        for (CallStateEnum factorGenerateTypeEnum : CallStateEnum.values()) {
            if (factorGenerateTypeEnum.getCode().equals(code)) {
                return factorGenerateTypeEnum.getName();
            }
        }
        return null;
    }
}
