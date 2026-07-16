package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FeatureDto extends BaseDto {
    @Schema(description = "实体ID")
    private Long entityId;
    @Schema(description = "实体名称")
    private String entityName;
    @Schema(description = "关联主键")
    private String joinKey;
    @Schema(description = "备注")
    private String remark;
}
