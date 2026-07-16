package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FeatureDefDto extends BaseDto {
    @Schema(description = "定义ID")
    private Long defId;
    @Schema(description = "特征名称")
    private String featureName;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "特征数据类型")
    private String objectType;
    @Schema(description = "转换逻辑")
    private String transform;
    @Schema(description = "版本")
    private String version;
    @Schema(description = "特征视图ID")
    private String viewId;
}
