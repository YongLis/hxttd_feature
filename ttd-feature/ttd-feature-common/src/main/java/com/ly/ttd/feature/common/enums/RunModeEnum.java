package com.ly.ttd.feature.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 运行模式枚举
 *
 * @author yong.li
 * @since 2026/3/28 17:46
 */
@Getter
@AllArgsConstructor
public enum RunModeEnum {
    TEST("test", "测试"),
    PROD("prod", "生产");

    private final String code;
    private final String name;

    public static String getByCode(String code) {
        for (RunModeEnum factorGenerateTypeEnum : RunModeEnum.values()) {
            if (factorGenerateTypeEnum.getCode().equals(code)) {
                return factorGenerateTypeEnum.getName();
            }
        }
        return null;
    }
}
