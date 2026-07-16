package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class ResourceChgDto {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "资源唯一标识键")
    private String resourceKey;
    @Schema(description = "资源类型")
    private String resourceType;
    @Schema(description = "操作类型(ADD/UPDATE/ROLLBACK/DELETE)")
    private String operationType;
    @Schema(description = "变更前版本号")
    private String beforeVersion;
    @Schema(description = "变更后版本号")
    private String afterVersion;
    @Schema(description = "变更前内容")
    private String beforeContent;
    @Schema(description = "变更后内容")
    private String afterContent;
    @Schema(description = "审核状态")
    private String auditStatus;
    @Schema(description = "创建时间")
    private Date crtTime;
    @Schema(description = "创建人")
    private String crtUser;
}
