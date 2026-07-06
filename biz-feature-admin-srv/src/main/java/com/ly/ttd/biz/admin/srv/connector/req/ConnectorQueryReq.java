package com.ly.ttd.biz.admin.srv.connector.req;

import com.ly.ttd.biz.admin.req.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 连接器列表查询请求
 *
 * @author yong.li
 * @since 2026-05-27
 */
@Data
public class ConnectorQueryReq extends PageQuery {

    /**
     * 资源标识键
     */
    @Schema(description = "资源标识键(模糊查询)", example = "JDBC_USER_DB")
    private String resourceKey;

    /**
     * 资源名称
     */
    @Schema(description = "资源名称(模糊查询)", example = "用户数据库")
    private String resourceName;

    /**
     * 连接器类型
     */
    @Schema(description = "连接器类型: JDBC/SQL", example = "JDBC")
    private String connectorType;
}
