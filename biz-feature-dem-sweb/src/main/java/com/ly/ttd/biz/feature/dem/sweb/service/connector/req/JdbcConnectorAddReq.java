package com.ly.ttd.biz.feature.dem.sweb.service.connector.req;

import com.ly.ttd.feature.common.model.DataFieldModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 连接器添加请求
 *
 * @author yong.li
 * @since 2026-05-27
 */
@Data
public class JdbcConnectorAddReq extends ConnectorAddReq {

    /**
     * JDBC配置：数据源名称
     */
    @Schema(description = "JDBC配置: 数据源名称")
    private String dataSourceName;

    /**
     * JDBC配置：SQL语句
     */
    @Schema(description = "JDBC配置: SQL语句(velocity模板语法)", example = "SELECT * FROM t_user WHERE id = :userId")
    private String sql;

    /**
     * JDBC配置：参数列表
     */
    @Schema(description = "JDBC配置: 参数列表")
    private List<DataFieldModel> fields;

    /**
     * JDBC配置：前置条件脚本
     */
    @Schema(description = "JDBC配置: 前置条件脚本")
    private String condition;
}
