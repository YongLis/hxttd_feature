package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccessPointParamDto extends BaseDto {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "接入点编码")
    private String accessPointCode;
    @Schema(description = "参数名称")
    private String paramName;
    @Schema(description = "版本号")
    private String version;
    @Schema(description = "参数编码")
    private String paramCode;
    @Schema(description = "参数类型(STRING/NUMBER/BOOLEAN/DATE)")
    private String paramType;
    @Schema(description = "是否必填(0-否,1-是)")
    private Integer required;
    @Schema(description = "默认值")
    private String defaultValue;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "排序顺序")
    private Integer sortOrder;
    @Schema(description = "父参数编码")
    private String parentParamCode;
    @Schema(description = "参数层级")
    private Integer paramLevel;
}
