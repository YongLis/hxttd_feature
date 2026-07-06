package com.ly.ttd.feature.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 因子取值类型
 *
 * @author yong.li
 * @since 2026/3/28 17:46
 */
@Getter
@AllArgsConstructor
public enum FactorMethodTypeEnum {
    SCRIPT("script", "脚本"),
    META_MAP("metaMap", "元数据");

    private final String code;
    private final String name;

    public static String getByCode(String code) {
        for (FactorMethodTypeEnum factorGenerateTypeEnum : FactorMethodTypeEnum.values()) {
            if (factorGenerateTypeEnum.getCode().equals(code)) {
                return factorGenerateTypeEnum.getName();
            }
        }
        return null;
    }
}
