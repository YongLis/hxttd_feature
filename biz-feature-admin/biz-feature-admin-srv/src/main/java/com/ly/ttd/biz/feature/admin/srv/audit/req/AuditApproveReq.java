package com.ly.ttd.biz.feature.admin.srv.audit.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 审核操作请求(通过/驳回)
 *
 * @author yong.li
 * @since 2026-05-30
 */
@Data
public class AuditApproveReq {

    /**
     * 审核记录ID
     */
    @NotNull(message = "审核记录ID不能为空")
    @Schema(description = "审核记录ID", example = "1")
    private Long id;

    /**
     * 审核结果(APPROVED/REJECTED)
     */
    @NotEmpty(message = "审核结果不能为空")
    @Schema(description = "审核结果: APPROVED/REJECTED", example = "APPROVED")
    private String auditStatus;

    /**
     * 审核意见
     */
    @Schema(description = "审核意见")
    private String auditComment;

    private String opUser;
}
