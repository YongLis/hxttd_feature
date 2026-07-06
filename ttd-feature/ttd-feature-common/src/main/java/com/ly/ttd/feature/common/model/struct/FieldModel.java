package com.ly.ttd.feature.common.model.struct;

import lombok.Data;

/**
 * 字段实体
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class FieldModel {
    /**
     * 字段编码
     */
    private String fieldCode;

    /**
     * 指标编码
     */
    private String factorCode;

    /**
     * 字段类型(NUMBER/STRING/BOOLEAN)
     */
    private String objectType;

    /**
     * 默认值
     */
    private String defaultValue;

}
