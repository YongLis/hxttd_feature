package com.ly.ttd.biz.feature.dem.sweb.service.dict.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 字典更新请求
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
public class DictUpdateReq  extends DictAddReq{

    @NotNull(message = "ID不能为空")
    @Schema(description = "字典ID", example = "1")
    private Long id;

    private Boolean deleted;
}
