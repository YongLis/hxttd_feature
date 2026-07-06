package com.ly.ttd.biz.admin.srv.audit.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 提交审核请求
 *
 * @author yong.li
 * @since 2026-05-30
 */
@Data
public class AuditSubmitReq {

    /**
     * 特征配置ID
     */
    @NotNull(message = "特征ID不能为空")
    @Schema(description = "特征配置ID", example = "1")
    private Long featureId;
}
