package com.ly.ttd.biz.admin.srv.audit.req;

import com.ly.ttd.biz.admin.req.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 审核列表查询请求
 *
 * @author yong.li
 * @since 2026-05-30
 */
@Data
public class AuditQueryReq extends PageQuery {

    @Schema(description = "资源类型: CONNECTOR/FACTOR/META_FIELD/FEATURE", example = "FEATURE")
    private String resourceType;

    @Schema(description = "资源标识键", example = "FCT_USER_AGE")
    private String resourceKey;

    /**
     * 审核状态(PENDING/APPROVED/REJECTED)
     */
    @Schema(description = "审核状态: PENDING/APPROVED/REJECTED", example = "PENDING")
    private String auditStatus;
}
