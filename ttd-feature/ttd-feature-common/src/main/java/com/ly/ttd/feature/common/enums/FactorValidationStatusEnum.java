package com.ly.ttd.feature.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 因子验证状态类型
 *
 * @author yong.li
 * @since 2026/3/28 17:46
 */
@Getter
@AllArgsConstructor
public enum FactorValidationStatusEnum {
    UNVERIFIED("0", "未验证"),
    VERIFIED("1", "验证");
    private final String code;
    private final String name;


    public static String getByCode(String code) {
        for (FactorValidationStatusEnum factorGenerateTypeEnum : FactorValidationStatusEnum.values()) {
            if (factorGenerateTypeEnum.getCode().equals(code)) {
                return factorGenerateTypeEnum.getName();
            }
        }
        return null;
    }
}
