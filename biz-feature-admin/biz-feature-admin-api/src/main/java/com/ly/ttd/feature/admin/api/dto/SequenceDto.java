package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SequenceDto {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "序列编码")
    private String seqCode;
    @Schema(description = "序列名称")
    private String seqName;
    @Schema(description = "序列值")
    private Long val;
}
