package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DictCodeDto extends BaseDto {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "字典ID")
    private Long dictId;
    @Schema(description = "字典Key")
    private String dictKey;
    @Schema(description = "字典Value")
    private String dictValue;
}
