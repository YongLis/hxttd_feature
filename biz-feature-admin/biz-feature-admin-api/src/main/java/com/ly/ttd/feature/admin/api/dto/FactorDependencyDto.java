package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FactorDependencyDto extends BaseDto {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "所属项目ID")
    private Long projectId;
    @Schema(description = "父节点标识")
    private String parent;
    @Schema(description = "父节点类型")
    private String parentType;
    @Schema(description = "子节点标识")
    private String child;
}
