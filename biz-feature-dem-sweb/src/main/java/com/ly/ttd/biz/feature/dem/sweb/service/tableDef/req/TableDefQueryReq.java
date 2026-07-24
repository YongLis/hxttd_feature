package com.ly.ttd.biz.feature.dem.sweb.service.tableDef.req;

import com.ly.ttd.base.result.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据表定义 分页查询请求
 *
 * @author yong.li
 * @since 2026-07-14
 */
@Data
public class TableDefQueryReq extends PageQuery {

    @Schema(description = "表名（模糊查询）")
    private String tableName;

    @Schema(description = "库名")
    private String dataSource;
}
