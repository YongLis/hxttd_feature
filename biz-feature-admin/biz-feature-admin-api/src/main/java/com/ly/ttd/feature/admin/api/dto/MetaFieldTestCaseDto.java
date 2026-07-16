package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MetaFieldTestCaseDto extends BaseDto {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "元字段编码")
    private String metaFieldCode;
    @Schema(description = "案例类型")
    private String caseType;
    @Schema(description = "交易号")
    private String bizOrderNo;
    @Schema(description = "案例内容")
    private String caseContent;
    @Schema(description = "目标值")
    private String targetValue;
}
