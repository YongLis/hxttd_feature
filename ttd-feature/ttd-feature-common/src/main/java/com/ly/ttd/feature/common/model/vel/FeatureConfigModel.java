package com.ly.ttd.feature.common.model.vel;

import lombok.Data;

import java.util.List;

/**
 * 特征配置实体
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class FeatureConfigModel {
    /**
     * 资源唯一标识键
     */
    private String resourceKey;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源版本号
     */
    private String version;

    /**
     * 所属项目ID
     */
    private Long projectId;

    /**
     * 特征编码
     */
    private String featureCode;
    private String mainDimension;
    // 从维度
    private String slaveDimension;
    /**
     * 默认值
     */
    private String defaultValue;
    private String exceptionValue;
    private Long timeout;


    /**
     * 脚本语言
     */
    private String language;

    /**
     * 条件脚本
     */
    private String conditionScript;

    /**
     * 主维度脚本
     */
    private String mainDimScript;

    /**
     * 从维度脚本
     */
    private String slaveDimScript;

    /**
     * 依赖元数据字段
     */
    private List<String> metaFields;

    /**
     * 返回值类型
     */
    private String returnType;

    /**
     * 指标值类型(FIX/DYNAMIC)
     */
    private String valueType;

    /**
     * 指标值脚本
     */
    private String valueScript;

    /**
     * 固定值
     */
    private String fixValue;

    /**
     * 聚合函数(SUM/COUNT/AVG/MIN/MAX)
     */
    private String aggregateMode;

    /**
     * 时间模式(TTL/DAY/MONTH/YEAR)
     */
    private String timeMode;

    /**
     * 时间单位
     */
    private String timeUnit;

    /**
     * 时间窗口
     */
    private Integer timeWindow;
}
