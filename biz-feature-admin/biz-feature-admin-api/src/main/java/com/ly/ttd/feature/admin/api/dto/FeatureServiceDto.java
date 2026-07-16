package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FeatureServiceDto extends BaseDto {
    @Schema(description = "服务ID")
    private Long serviceId;
    @Schema(description = "服务名称")
    private String serviceName;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "特征名称列表(逗号分隔)")
    private String featureList;
}
