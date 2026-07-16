package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class AuditDto {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "资源类型")
    private String resourceType;
    @Schema(description = "资源标识键")
    private String resourceKey;
    @Schema(description = "资源名称")
    private String resourceName;
    @Schema(description = "审核状态(PENDING/APPROVED/REJECTED)")
    private String auditStatus;
    @Schema(description = "操作类型")
    private String operationType;
    @Schema(description = "变更前内容")
    private String beforeContent;
    @Schema(description = "变更后内容")
    private String afterContent;
    @Schema(description = "审核备注")
    private String auditComment;
    @Schema(description = "提交人")
    private String submitUser;
    @Schema(description = "审核人")
    private String auditUser;
    @Schema(description = "提交时间")
    private Date submitTime;
    @Schema(description = "审核时间")
    private Date auditTime;
}
