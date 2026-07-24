package com.ly.ttd.biz.feature.dem.sweb.service.tableDef.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 数据表列 详情
 *
 * @author yong.li
 * @since 2026-07-14
 */
@Data
public class TableColumnDetail {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "表名")
    private String tableName;

    @Schema(description = "列名")
    private String columnName;

    @Schema(description = "列类型")
    private String columnType;

    @Schema(description = "是否允许为空：Y是 N否")
    private String nullAble;

    @Schema(description = "指标编码")
    private String factorCode;

    @Schema(description = "创建人")
    private String crtUser;

    @Schema(description = "创建时间")
    private Date crtTime;
}
