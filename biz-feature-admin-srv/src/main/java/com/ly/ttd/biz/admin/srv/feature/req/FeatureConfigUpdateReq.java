package com.ly.ttd.biz.admin.srv.feature.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 特征配置更新请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FeatureConfigUpdateReq extends FeatureConfigAddReq {

    @NotNull(message = "ID不能为空")
    @Schema(description = "特征配置ID", example = "1")
    private Long id;

}
