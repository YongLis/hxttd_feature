package com.ly.ttd.feature.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 因子状态类型
 *
 * @author yong.li
 * @since 2026/3/28 17:46
 */
@Getter
@AllArgsConstructor
public enum FactorStatusEnum {
    TO_BE_SUBMITTED("to_be_submitted", "待提交"),
    TO_BE_REVIEWED("to_be_reviewed", "待审核"),
    NORMAL("normal", "正常"),
    FROZEN("frozen", "冻结"),
    DELETED("deleted", "已删除");;

    private final String code;
    private final String name;

    public static String getByCode(String code) {
        for (FactorStatusEnum factorGenerateTypeEnum : FactorStatusEnum.values()) {
            if (factorGenerateTypeEnum.getCode().equals(code)) {
                return factorGenerateTypeEnum.getName();
            }
        }
        return null;
    }
}
