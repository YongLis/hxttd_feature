package com.ly.ttd.feature.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字段来源
 *
 * @author yong.li
 * @since 2026/4/15 15:57
 */
@Getter
@AllArgsConstructor
public enum FieldSourceTypeEnum {
    SELF("self", "自定义"),
    FACTOR("factor", "指标");


    private String code;
    private String name;
}
