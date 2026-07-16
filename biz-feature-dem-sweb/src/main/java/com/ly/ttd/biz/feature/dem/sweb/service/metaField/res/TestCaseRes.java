package com.ly.ttd.biz.feature.dem.sweb.service.metaField.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 元字段测试响应
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class TestCaseRes {

    /**
     * 是否执行成功
     */
    @Schema(description = "是否执行成功")
    private boolean success;

    /**
     * 执行结果
     */
    @Schema(description = "执行结果")
    private Object result;

    /**
     * 结果类型
     */
    @Schema(description = "结果类型")
    private String resultType;

    /**
     * 错误信息
     */
    @Schema(description = "错误信息")
    private String errorMessage;

    /**
     * 执行耗时（毫秒）
     */
    @Schema(description = "执行耗时（毫秒）")
    private long executionTime;

    /**
     * 元字段信息
     */
    @Schema(description = "资源唯一标识键")
    private String resourceKey;

    @Schema(description = "资源名称")
    private String resourceName;

    @Schema(description = "脚本语言")
    private String language;
}
