package com.ly.ttd.biz.feature.dem.sweb.service.metaField.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 元字段更新请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class MetaFieldUpdateReq extends MetaFieldAddReq {

    @NotNull(message = "ID不能为空")
    @Schema(description = "元字段ID", example = "1")
    private Long id;
}
