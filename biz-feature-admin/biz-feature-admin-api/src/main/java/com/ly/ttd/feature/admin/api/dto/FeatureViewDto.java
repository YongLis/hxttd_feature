package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FeatureViewDto extends BaseDto {
    @Schema(description = "视图ID")
    private Long viewId;
    @Schema(description = "视图名称")
    private String viewName;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "实体ID")
    private Long entityId;
    @Schema(description = "数据来源表")
    private String sourceTable;
    @Schema(description = "视图类型(offline/online/batch)")
    private String viewType;
    @Schema(description = "数据生命周期(天)")
    private Integer lifecycle;
}
