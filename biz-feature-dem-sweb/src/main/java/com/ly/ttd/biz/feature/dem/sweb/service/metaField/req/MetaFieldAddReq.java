package com.ly.ttd.biz.feature.dem.sweb.service.metaField.req;

import com.ly.ttd.biz.feature.dem.sweb.req.ResourceReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 元字段添加请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class MetaFieldAddReq extends ResourceReq {

    @NotBlank(message = "脚本语言不能为空")
    @Schema(description = "脚本语言: groovy/aviator", example = "groovy")
    private String language;

    @NotBlank(message = "计算脚本不能为空")
    @Schema(description = "计算脚本", example = "return input.get('user_id')")
    private String script;

    @NotBlank(message = "返回值类型不能为空")
    @Schema(description = "返回值类型: STRING/LONG/DOUBLE/DECIMAL/BOOLEAN/LIST/DATE/DICT", example = "LONG")
    private String returnType;

    @Size(max = 64, message = "分类标签不能超过64个字符")
    @Schema(description = "分类标签")
    private String categoryTag;
}
