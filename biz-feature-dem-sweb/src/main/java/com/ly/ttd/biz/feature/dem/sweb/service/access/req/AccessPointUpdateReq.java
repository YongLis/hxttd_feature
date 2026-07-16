package com.ly.ttd.biz.feature.dem.sweb.service.access.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 接入点更新请求
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
public class AccessPointUpdateReq extends AccessPointAddReq {

    @NotNull(message = "ID不能为空")
    @Schema(description = "接入点ID", example = "1")
    private Long id;
}
