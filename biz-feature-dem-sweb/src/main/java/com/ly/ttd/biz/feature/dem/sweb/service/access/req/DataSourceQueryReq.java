package com.ly.ttd.biz.feature.dem.sweb.service.access.req;


import com.ly.ttd.base.result.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据源查询请求
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
public class DataSourceQueryReq extends PageQuery {

    @Schema(description = "数据源类别: JDBC/REDIS/ES/HTTP", example = "JDBC")
    private String sourceCategory;

    @Schema(description = "数据源编码(模糊查询)", example = "DS_USER_DB")
    private String sourceCode;

    @Schema(description = "数据源名称(模糊查询)", example = "用户数据库")
    private String sourceName;
}
