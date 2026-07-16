package com.ly.ttd.feature.admin.api.dto;

import com.ly.ttd.feature.common.model.DataFieldModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/7/10 15:04
 */
@Data
public class HttpConnectorDto extends BaseDto {

    private Long id;


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


    /**
     * 前置条件
     */
    private String condition;
    /**
     * 参数列表
     */
    private List<DataFieldModel> fields;

    @Schema(description = "返回值类型: STRING/LONG/DOUBLE/DECIMAL/BOOLEAN/LIST/DATE/DICT", example = "LONG")
    private String returnType;

    @Schema(description = "超时时间(毫秒)", example = "3000")
    private Long timeout;
    // ========== HTTP 连接器配置 ==========

    @Schema(description = "HTTP配置: 请求URL")
    private String url;

    @Schema(description = "HTTP配置: 请求方法(GET/POST/PUT/DELETE)")
    private String method;

    @Schema(description = "HTTP配置: 请求头")
    private Map<String, Object> header;

    @Schema(description = "HTTP配置: 请求参数")
    private Map<String, Object> param;

}
