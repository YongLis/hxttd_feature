package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TableColumnDto extends BaseDto {
    @Schema(description = "主键ID")
    private String id;
    @Schema(description = "表名")
    private String tableName;
    @Schema(description = "列名")
    private String columnName;
    @Schema(description = "列类型")
    private String columnType;
    @Schema(description = "是否允许为空(Y/N)")
    private String nullAble;
    @Schema(description = "指标编码")
    private String factorCode;
}
