package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectDto extends BaseDto {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "项目编码")
    private String projectCode;
    @Schema(description = "项目名称")
    private String name;
}
