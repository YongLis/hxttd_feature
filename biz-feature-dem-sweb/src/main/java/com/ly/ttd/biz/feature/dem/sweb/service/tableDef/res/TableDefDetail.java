package com.ly.ttd.biz.feature.dem.sweb.service.tableDef.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 数据表定义 详情
 *
 * @author yong.li
 * @since 2026-07-14
 */
@Data
public class TableDefDetail {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "表名")
    private String tableName;

    @Schema(description = "库名")
    private String dataSource;

    @Schema(description = "关联topic")
    private String topic;

    @Schema(description = "字段数量")
    private Integer columnCount;

    @Schema(description = "字段列表")
    private List<TableColumnDetail> columns;

    @Schema(description = "创建人")
    private String crtUser;

    @Schema(description = "创建时间")
    private Date crtTime;

    @Schema(description = "修改人")
    private String uptUser;

    @Schema(description = "修改时间")
    private Date uptTime;

    @Schema(description = "是否删除")
    private Boolean deleted;
}
