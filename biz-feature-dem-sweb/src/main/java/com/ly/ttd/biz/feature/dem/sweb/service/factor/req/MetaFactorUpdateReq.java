package com.ly.ttd.biz.feature.dem.sweb.service.factor.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 元字段指标更新请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class MetaFactorUpdateReq extends MetaFactorAddReq {

    @NotNull(message = "ID不能为空")
    @Schema(description = "指标ID", example = "1")
    private Long id;
}
