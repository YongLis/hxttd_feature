package com.ly.ttd.feature.common.model.meta;

import lombok.Data;

/**
 * 元字段测试用例
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class MetaFieldTestCaseModel {

    /**
     * 元字段类型
     */
    private String metaFieldCode;

    /**
     * 测试用例类型
     */
    private String caseType;

    /**
     * 引用交易号
     */

    private String bizOrderNo;

    /**
     * 用例内容
     */
    private String caseContent;

    /**
     * 目标值
     */

    private String targetValue;
}
