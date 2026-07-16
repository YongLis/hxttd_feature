package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DataStructDto extends BaseDto {
    @Schema(description = "主键ID")
    private Long id;

    private Long projectId;

    @Schema(description = "资源唯一标识键")
    private String resourceKey;
    @Schema(description = "资源名称")
    private String resourceName;
    @Schema(description = "版本号")
    private String version;
}
