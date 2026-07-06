package com.ly.ttd.feature.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 实时特征条件表达式类型
 *
 * @author yong.li
 * @since 2026/3/28 17:46
 */
@Getter
@AllArgsConstructor
public enum VelocityExpressionTypeEnum {
    EQUAL("==", "等于"),
    LESS_THAN("<", "小于"),
    GREATER_THAN(">", "大于"),
    GREATER_THAN_OR_EQUAL(">=", "大于等于"),
    LESS_THAN_OR_EQUAL("<=", "小于等于"),
    NOT_EQUAL("!=", "不等于"),
    IN("in", "在集合中"),
    NOT_IN("not_in", "不在集合中"),
    CONTAINS("contains", "字符串包含"),
    EXIST("exist", "存在交集"),
    ;

    private final String code;
    private final String name;

    public static String getByCode(String code) {
        for (VelocityExpressionTypeEnum factorGenerateTypeEnum : VelocityExpressionTypeEnum.values()) {
            if (factorGenerateTypeEnum.getCode().equals(code)) {
                return factorGenerateTypeEnum.getName();
            }
        }
        return null;
    }
}
