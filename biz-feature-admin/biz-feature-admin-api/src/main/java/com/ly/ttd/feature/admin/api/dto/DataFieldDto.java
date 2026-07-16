package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DataFieldDto {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "数据集编码")
    private String dataStructCode;
    @Schema(description = "字段编码")
    private String fieldCode;
    @Schema(description = "字段名称")
    private String fieldName;
    @Schema(description = "指标编码")
    private String factorCode;
    @Schema(description = "字段类型(NUMBER/STRING/BOOLEAN)")
    private String objectType;
    @Schema(description = "默认值")
    private String defaultValue;
    @Schema(description = "排序顺序")
    private Integer sortOrder;
}
