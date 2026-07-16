package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TableDefDto extends BaseDto {
    @Schema(description = "主键ID")
    private String id;
    @Schema(description = "表名")
    private String tableName;
    @Schema(description = "库名")
    private String dataSource;

    private List<TableColumnDto> columns;
}
