package com.ly.ttd.biz.feature.admin.srv.resource.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author yong.li
 * @since 2026/6/23 13:58
 */
@Data
public class ResourceChgReq {
    /**
     * 资源唯一标识键
     */
    @Schema(description = "资源唯一标识键", example = "USER_INFO")
    private String resourceKey;

    /**
     * 资源类型
     */
    @Schema(description = "资源类型: CONNECTOR/FACTOR/META_FIELD/FEATURE", example = "FEATURE")
    private String resourceType;

    /**
     * 操作类型：ADD/UPDATE/ROLLBACK/DELETE
     */
    @Schema(description = "操作类型: ADD/UPDATE/ROLLBACK/DELETE", example = "UPDATE")
    private String operationType;

    /**
     * 变更前版本号
     */
    @Schema(description = "变更前版本号")
    private String beforeVersion;

    /**
     * 变更后版本号
     */
    @Schema(description = "变更后版本号")
    private String afterVersion;

    /**
     * 变更前资源内容
     */
    @Schema(description = "变更前资源内容(JSON)")
    private String beforeContent;

    /**
     * 变更后资源内容
     */
    @Schema(description = "变更后资源内容(JSON)")
    private String afterContent;

    /**
     * 审核状态
     */
    @Schema(description = "审核状态: PENDING/APPROVED/REJECTED", example = "PENDING")
    private String auditStatus;


    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String crtUser;

    public ResourceChgReq(String resourceKey, String resourceType, String operationType, String beforeVersion, String afterVersion, String beforeContent, String afterContent, String auditStatus, String crtUser) {
        this.resourceKey = resourceKey;
        this.resourceType = resourceType;
        this.operationType = operationType;
        this.beforeVersion = beforeVersion;
        this.afterVersion = afterVersion;
        this.beforeContent = beforeContent;
        this.afterContent = afterContent;
        this.auditStatus = auditStatus;
        this.crtUser = crtUser;
    }
}
