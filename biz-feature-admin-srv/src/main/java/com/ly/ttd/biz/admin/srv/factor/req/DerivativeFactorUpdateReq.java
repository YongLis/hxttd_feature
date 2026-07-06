package com.ly.ttd.biz.admin.srv.factor.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 衍生指标
 *
 * @author yong.li
 * @since 2026/7/2 09:35
 */
@Data
public class DerivativeFactorUpdateReq extends DerivativeFactorAddReq {
    @NotNull(message = "ID不能为空")
    @Schema(description = "指标ID", example = "1")
    private Long id;
}
