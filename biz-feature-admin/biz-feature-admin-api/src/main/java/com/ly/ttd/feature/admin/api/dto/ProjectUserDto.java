package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectUserDto extends BaseDto {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "项目ID")
    private Long projectId;
    @Schema(description = "用户账户")
    private String userAccount;
}
