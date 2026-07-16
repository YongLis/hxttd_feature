package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FeatureDataStoreDto extends BaseDto {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "实体键")
    private String entityKey;
    @Schema(description = "特征名称")
    private String featureName;
    @Schema(description = "特征值")
    private String featureValue;
    @Schema(description = "特征值类型")
    private String objectType;
}
