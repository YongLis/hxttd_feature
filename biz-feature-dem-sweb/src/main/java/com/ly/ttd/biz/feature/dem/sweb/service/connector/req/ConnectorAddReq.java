package com.ly.ttd.biz.feature.dem.sweb.service.connector.req;

import com.ly.ttd.base.result.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

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
}
