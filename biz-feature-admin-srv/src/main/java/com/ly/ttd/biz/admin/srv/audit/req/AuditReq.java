package com.ly.ttd.biz.admin.srv.audit.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author yong.li
 * @since 2026/6/23 12:58
 */
@Data
public class AuditReq {
    @NotBlank
    @Schema(description = "资源标识键", example = "FCT_USER_AGE")
    private String resourceKey;

    @NotBlank
    @Schema(description = "资源类型: CONNECTOR/FACTOR/META_FIELD/FEATURE", example = "FEATURE")
    private String resourceType;

    @NotBlank
    @Schema(description = "资源名称", example = "用户年龄")
    private String resourceName;

    @NotBlank
    @Schema(description = "操作类型: ADD/UPDATE/ROLLBACK/DELETE", example = "UPDATE")
    private String operationType;

    @Schema(description = "变更前内容(JSON)")
    private String beforeJson;

    @Schema(description = "变更后内容(JSON)")
    private String afterJson;

    public AuditReq(String resourceKey, String resourceType, String resourceName, String operationType, String beforeJson, String afterJson) {
        this.resourceKey = resourceKey;
        this.resourceType = resourceType;
        this.resourceName = resourceName;
        this.operationType = operationType;
        this.beforeJson = beforeJson;
        this.afterJson = afterJson;
    }
}
