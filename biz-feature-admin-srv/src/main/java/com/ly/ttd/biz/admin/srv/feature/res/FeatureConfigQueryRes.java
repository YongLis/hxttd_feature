package com.ly.ttd.biz.admin.srv.feature.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 特征配置查询响应
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class FeatureConfigQueryRes {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "资源唯一标识键")
    private String resourceKey;

    @Schema(description = "资源名称")
    private String resourceName;

    @Schema(description = "版本号")
    private String version;

    @Schema(description = "所属项目ID")
    private Long projectId;

    @Schema(description = "特征编码")
    private String featureCode;

    @Schema(description = "脚本语言")
    private String language;

    @Schema(description = "返回值类型")
    private String returnType;

    @Schema(description = "指标值类型(FIX/DYNAMIC)")
    private String valueType;

    @Schema(description = "条件脚本")
    private String conditionScript;

    @Schema(description = "主维度脚本")
    private String mainDimScript;

    @Schema(description = "从维度脚本")
    private String slaveDimScript;

    @Schema(description = "依赖元数据字段(JSON数组)")
    private String metaFields;

    @Schema(description = "指标值脚本")
    private String valueScript;

    @Schema(description = "固定值")
    private String fixValue;

    @Schema(description = "聚合函数(SUM/COUNT/AVG/MIN/MAX)")
    private String aggregateMode;

    @Schema(description = "时间模式(TTL/DAY/MONTH/YEAR)")
    private String timeMode;

    @Schema(description = "时间单位")
    private String timeUnit;

    @Schema(description = "时间窗口")
    private Integer timeWindow;

    @Schema(description = "创建人")
    private String crtUser;

    @Schema(description = "创建时间")
    private Date crtTime;
}
