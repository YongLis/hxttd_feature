package com.ly.ttd.biz.feature.dem.sweb.service.metaField.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 元字段测试请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class TestCaseReq {

    @NotBlank(message = "资源键不能为空")
    @Schema(description = "资源键", example = "1")
    private String resourceKey;

    @NotBlank(message = "语言不能为空")
    @Schema(description = "语言", example = "groovy")
    private String language;

    @NotBlank(message = "脚本不能为空")
    @Schema(description = "脚本", example = "return 'hello world';")
    private String script;

    /**
     * 测试输入参数 (JSON key-value)
     */
    @Schema(description = "测试输入参数(JSON key-value)", example = "{\"userId\": \"123\"}")
    private String jsonData;
}
