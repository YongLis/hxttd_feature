package com.ly.ttd.biz.feature.dem.sweb.service.dict.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 字典添加请求
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
public class DictAddReq {

    @NotBlank(message = "系统编码不能为空")
    @Schema(description = "系统编码", example = "SYS_COMMON")
    private String systemCode;

    @NotBlank(message = "字典编码不能为空")
    @Schema(description = "字典编码", example = "DICT_GENDER")
    private String dictCode;

    @NotBlank(message = "字典名称不能为空")
    @Schema(description = "字典名称", example = "性别")
    private String dictName;
}
