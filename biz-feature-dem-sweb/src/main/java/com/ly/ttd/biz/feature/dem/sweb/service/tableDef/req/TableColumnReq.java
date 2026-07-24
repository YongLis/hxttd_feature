package com.ly.ttd.biz.feature.dem.sweb.service.tableDef.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 数据表列 请求参数
 *
 * @author yong.li
 * @since 2026-07-14
 */
@Data
public class TableColumnReq {

    @Schema(description = "主键ID（更新时传递）")
    private String id;

    @NotBlank(message = "列名不能为空")
    @Schema(description = "列名")
    private String columnName;

    @NotBlank(message = "列类型不能为空")
    @Schema(description = "列类型")
    private String columnType;

    @NotBlank(message = "是否允许为空不能为空")
    @Schema(description = "是否允许为空：Y是 N否")
    private String nullAble;

    @Schema(description = "指标编码")
    private String factorCode;
}
