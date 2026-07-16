package com.ly.ttd.biz.feature.dem.sweb.service.feature.req;

import com.ly.ttd.biz.feature.dem.sweb.req.ResourceReq;
import com.ly.ttd.feature.admin.api.dto.ColumnCondition;
import com.ly.ttd.feature.admin.api.dto.FeatureConfigForm;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 特征配置新增请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class FeatureConfigAddReq extends ResourceReq {
    @Schema(description = "特征编码", example = "FCT_USER_AGE")
    private String featureCode;

    /**
     * 前置条件
     */
    @Schema(description = "前置条件列表")
    private List<ColumnCondition> conditions;

    @NotBlank(message = "返回值类型不能为空")
    @Schema(description = "返回值类型: STRING/LONG/DOUBLE/DECIMAL/BOOLEAN/LIST/DATE/DICT", example = "LONG")
    private String returnType;


    // 主维度
    @NotBlank(message = "主维度不允许为空")
    @Schema(description = "主维度", example = "user_id")
    private String mainDimension;
    // 从维度
    @NotBlank(message = "从维度不允许为空")
    @Schema(description = "从维度", example = "order_id")
    private String slaveDimension;

    @NotBlank(message = "指标值类型不能为空")
    @Schema(description = "指标值类型: FEATURE/META_FIELD/FACTOR", example = "FEATURE")
    private String valueType;

    @Schema(description = "值维度")
    private String valueDimension;

    @Schema(description = "固定值")
    private String fixValue;

    @Schema(description = "聚合模式: SUM/COUNT/AVG/MAX/MIN", example = "SUM")
    private String aggregateMode;

    @Schema(description = "时间模式")
    private String timeMode;

    @Schema(description = "时间单位", example = "DAY")
    private String timeUnit;

    @Schema(description = "时间窗口", example = "7")
    private Integer timeWindow;


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
