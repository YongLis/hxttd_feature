package com.ly.ttd.biz.admin.srv.connector.req;

import com.ly.ttd.biz.admin.req.BaseRequest;
import com.ly.ttd.feature.common.model.DataFieldModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 连接器添加请求
 *
 * @author yong.li
 * @since 2026-05-27
 */
@Data
public class ConnectorAddReq extends BaseRequest {

    @NotEmpty(message = "资源标识键不能为空")
    @Schema(description = "资源标识键", example = "JDBC_USER_DB")
    private String resourceKey;

    @NotEmpty(message = "资源名称不能为空")
    @Schema(description = "资源名称", example = "用户数据库")
    private String resourceName;

    @Schema(description = "版本号")
    private String version;

    @Schema(description = "项目ID", example = "1")
    private Long projectId;

    @NotEmpty(message = "连接器类型不能为空")
    @Schema(description = "连接器类型: JDBC/SQL", example = "JDBC")
    private String connectorType;

    @Schema(description = "默认值")
    private String defaultValue;

    @Schema(description = "异常值")
    private String exceptionValue;

    @Schema(description = "返回值类型: STRING/LONG/DOUBLE/DECIMAL/BOOLEAN/LIST/DATE/DICT", example = "LONG")
    private String returnType;

    @Schema(description = "超时时间(毫秒)", example = "3000")
    private Long timeout;

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

    // ========== HTTP 连接器配置 ==========

    @Schema(description = "HTTP配置: 请求URL")
    private String url;

    @Schema(description = "HTTP配置: 请求方法(GET/POST/PUT/DELETE)")
    private String method;

    @Schema(description = "HTTP配置: 请求头")
    private Map<String, Object> header;

    @Schema(description = "HTTP配置: 请求参数")
    private Map<String, Object> param;

    // ========== ES 连接器配置 ==========

    @Schema(description = "ES配置: 索引端点")
    private String endpoint;

    @Schema(description = "ES配置: DSL查询语句")
    private String dsl;
}
