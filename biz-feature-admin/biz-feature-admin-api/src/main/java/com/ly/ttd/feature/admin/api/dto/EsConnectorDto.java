package com.ly.ttd.feature.admin.api.dto;

import com.ly.ttd.feature.common.model.DataFieldModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class EsConnectorDto extends BaseDto {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "资源唯一标识键")
    private String resourceKey;
    @Schema(description = "资源名称")
    private String resourceName;
    @Schema(description = "版本号")
    private String version;
    @Schema(description = "所属项目ID")
    private Long projectId;
    @Schema(description = "连接器类型")
    private String connectorType;
    @Schema(description = "默认值")
    private String defaultValue;
    @Schema(description = "异常值")
    private String exceptionValue;
    @Schema(description = "超时时间(毫秒)")
    private Long timeout;
    @Schema(description = "资源JSON配置")
    private String resourceJson;

    /**
     * 参数列表
     */
    @Schema(description = "参数列表")
    private List<DataFieldModel> fields;

    /**
     * 前置条件脚本
     */
    @Schema(description = "前置条件脚本")
    private String condition;

    // ========== ES 连接器配置 ==========
    @Schema(description = "ES配置: 索引端点")
    private String endpoint;

    @Schema(description = "ES配置: DSL查询语句")
    private String dsl;

    private String returnType;


}
