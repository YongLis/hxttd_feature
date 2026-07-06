package com.ly.ttd.biz.admin.srv.feature.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author yong.li
 * @since 2026/6/2 15:36
 */
@Data
public class FeatureConfigForm {

    @Schema(description = "资源标识键")
    private String resourceKey;

    @Schema(description = "资源名称")
    private String resourceName;

    @Schema(description = "版本号")
    private String version;

    @Schema(description = "项目ID", example = "1")
    private Long projectId;


    @Schema(description = "特征编码", example = "FCT_USER_AGE")
    private String featureCode;

    /**
     * 默认值
     */
    @Schema(description = "默认值")
    private String defaultValue;

    @Schema(description = "异常值")
    private String exceptionValue;

    @Schema(description = "超时时间(毫秒)", example = "3000")
    private Long timeout;

    /**
     * 前置条件
     */
    @Schema(description = "前置条件列表")
    private List<ColumnCondition> conditions;

    @Schema(description = "返回值类型: STRING/LONG/DOUBLE/DECIMAL/BOOLEAN/LIST/DATE/DICT", example = "LONG")
    private String returnType;


    // 主维度
    @Schema(description = "主维度", example = "user_id")
    private String mainDimension;
    // 从维度
    @Schema(description = "从维度", example = "order_id")
    private String slaveDimension;

    @Schema(description = "值类型", example = "FEATURE")
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

}
