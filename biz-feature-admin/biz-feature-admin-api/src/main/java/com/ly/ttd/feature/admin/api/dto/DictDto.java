package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DictDto extends BaseDto {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "系统编码")
    private String systemCode;
    @Schema(description = "字典编码")
    private String dictCode;
    @Schema(description = "字典名称")
    private String dictName;
}
