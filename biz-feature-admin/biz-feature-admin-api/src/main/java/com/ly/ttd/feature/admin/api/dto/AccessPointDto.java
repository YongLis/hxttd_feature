package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccessPointDto extends BaseDto {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "接入点编码")
    private String code;
    @Schema(description = "接入点名称")
    private String name;
    @Schema(description = "版本号")
    private String version;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "所属项目ID")
    private Long projectId;
    @Schema(description = "API JSON配置")
    private String apiJson;

    private List<AccessPointParamDto> paramItems;

}
