package com.ly.ttd.biz.admin.srv.dict.req;

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
public class DictUpdateReq {

    @NotNull(message = "ID不能为空")
    @Schema(description = "字典ID", example = "1")
    private Long id;

    @Schema(description = "系统编码", example = "SYS_COMMON")
    private String systemCode;

    @Schema(description = "字典编码", example = "DICT_GENDER")
    private String dictCode;

    @Schema(description = "字典名称", example = "性别")
    private String dictName;
}
