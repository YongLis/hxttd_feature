package com.ly.ttd.biz.admin.srv.metaField.req;

import com.ly.ttd.biz.admin.req.BaseRequest;
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
public class MetaFieldUpdateReq extends BaseRequest {

    @NotNull(message = "ID不能为空")
    @Schema(description = "元字段ID", example = "1")
    private Long id;

    @Schema(description = "资源名称", example = "用户ID")
    private String resourceName;

    @Schema(description = "脚本语言: groovy/aviator", example = "groovy")
    private String language;

    @Schema(description = "计算脚本", example = "return input.get('user_id')")
    private String script;

    @Schema(description = "返回值类型: STRING/LONG/DOUBLE/DECIMAL/BOOLEAN/LIST/DATE/DICT", example = "LONG")
    private String returnType;

    @Schema(description = "默认值")
    private String defaultValue;

    @Schema(description = "异常值")
    private String exceptionValue;

    @Schema(description = "分类标签")
    private String categoryTag;
}
