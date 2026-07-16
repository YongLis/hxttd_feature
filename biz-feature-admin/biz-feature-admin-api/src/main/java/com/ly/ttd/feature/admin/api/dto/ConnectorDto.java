package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ConnectorDto extends BaseDto {
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
}
