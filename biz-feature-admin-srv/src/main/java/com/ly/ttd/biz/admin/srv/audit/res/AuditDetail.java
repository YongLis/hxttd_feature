package com.ly.ttd.biz.admin.srv.audit.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 审核详情响应（结构化，包含解析后的配置对象）
 *
 * @author yong.li
 * @since 2026-05-30
 */
@Data
public class AuditDetail {

    @Schema(description = "主键ID")
    private Long id;

    /**
     * 关联特征类型
     */
    @Schema(description = "关联特征类型")
    private String resourceType;

    /**
     * 关联特征资源key
     */
    @Schema(description = "关联特征资源key")
    private String resourceKey;

    /**
     * 资源名称
     */
    @Schema(description = "资源名称")
    private String resourceName;

    /**
     * 审核状态(PENDING=待审核, APPROVED=已通过, REJECTED=已驳回)
     */
    @Schema(description = "审核状态(PENDING=待审核, APPROVED=已通过, REJECTED=已驳回)", example = "PENDING")
    private String auditStatus;

    /**
     * 操作类型：ADD/UPDATE/DELETE
     */
    @Schema(description = "操作类型：ADD/UPDATE/DELETE", example = "ADD")
    private String operationType;

    // 变更前的配置
    @Schema(description = "变更前的配置")
    private String beforeContent;

    // 变更后的配置
    @Schema(description = "变更后的配置")
    private String afterContent;

    /**
     * 审核意见
     */
    @Schema(description = "审核意见")
    private String auditComment;

    /**
     * 提交人
     */
    @Schema(description = "提交人")
    private String submitUser;

    /**
     * 审核人
     */
    @Schema(description = "审核人")
    private String auditUser;

    /**
     * 提交时间
     */
    @Schema(description = "提交时间")
    private Date submitTime;

    /**
     * 审核时间
     */
    @Schema(description = "审核时间")
    private Date auditTime;

}
