package com.ly.ttd.biz.feature.dem.sweb.service.dict.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 字典键值更新请求
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
public class DictCodeUpdateReq {

    @NotNull(message = "ID不能为空")
    @Schema(description = "字典键值ID", example = "1")
    private Long id;

    @Schema(description = "字典ID", example = "1")
    private Long dictId;

    @Schema(description = "字典键", example = "MALE")
    private String dictKey;

    @Schema(description = "字典值", example = "男")
    private String dictValue;
}
