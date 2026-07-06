package com.ly.ttd.biz.admin.srv.dict.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 字典键值添加请求
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
public class DictCodeAddReq {

    @NotNull(message = "字典ID不能为空")
    @Schema(description = "字典ID", example = "1")
    private Long dictId;

    @NotBlank(message = "字典key不能为空")
    @Schema(description = "字典键", example = "MALE")
    private String dictKey;

    @NotBlank(message = "字典value不能为空")
    @Schema(description = "字典值", example = "男")
    private String dictValue;
}
