package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class FeatureConfigDto extends BaseDto {
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
    @Schema(description = "默认值")
    private String defaultValue;
    @Schema(description = "异常值")
    private String exceptionValue;
    @Schema(description = "超时时间(毫秒)")
    private Long timeout;
    @Schema(description = "主维度")
    private String mainDimension;
    @Schema(description = "从维度")
    private String slaveDimension;
    @Schema(description = "脚本语言")
    private String language;
    @Schema(description = "条件脚本")
    private String conditionScript;
    @Schema(description = "主维度脚本")
    private String mainDimScript;
    @Schema(description = "从维度脚本")
    private String slaveDimScript;
    @Schema(description = "依赖元数据字段(JSON数组)")
    private String metaFields;
    @Schema(description = "返回值类型")
    private String returnType;
    @Schema(description = "指标值类型(FIX/DYNAMIC)")
    private String valueType;
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
    @Schema(description = "资源JSON配置")
    private String resourceJson;


    /**
     * 前置条件
     */
    @Schema(description = "前置条件列表")
    private List<ColumnCondition> conditions;


    @Schema(description = "值维度")
    private String valueDimension;


    public FeatureConfigForm buildForm() {
        FeatureConfigForm form = new FeatureConfigForm();
        form.setResourceKey(getResourceKey());
        form.setResourceName(getResourceName());
        form.setVersion(getVersion());
        form.setProjectId(getProjectId());
        form.setFeatureCode(featureCode);
        form.setDefaultValue(getDefaultValue());
        form.setExceptionValue(getExceptionValue());
        form.setTimeout(getTimeout());
        form.setConditions(conditions);
        form.setReturnType(returnType);
        form.setMainDimension(mainDimension);
        form.setSlaveDimension(slaveDimension);
        form.setValueType(valueType);
        form.setValueDimension(valueDimension);
        form.setFixValue(fixValue);
        form.setAggregateMode(aggregateMode);
        form.setTimeMode(timeMode);
        form.setTimeUnit(timeUnit);
        form.setTimeWindow(timeWindow);
        return form;
    }

}
