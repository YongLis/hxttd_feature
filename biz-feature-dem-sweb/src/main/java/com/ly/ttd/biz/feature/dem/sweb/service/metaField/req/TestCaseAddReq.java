package com.ly.ttd.biz.feature.dem.sweb.service.metaField.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 测试用例新增请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class TestCaseAddReq {

    @NotBlank(message = "元字段编码不能为空")
    @Schema(description = "元字段编码", example = "META_USER_ID")
    private String metaFieldCode;

    @NotBlank(message = "案例类型不能为空")
    @Schema(description = "案例类型", example = "NORMAL")
    private String caseType;

    @NotBlank(message = "交易号不能为空")
    @Schema(description = "交易号", example = "BIZ20260701001")
    private String bizOrderNo;

    @Schema(description = "案例内容(JSON输入参数)")
    private String caseContent;

    @Schema(description = "目标值")
    private String targetValue;
}
