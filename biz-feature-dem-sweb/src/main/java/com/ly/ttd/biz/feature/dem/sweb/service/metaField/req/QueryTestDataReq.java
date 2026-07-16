package com.ly.ttd.biz.feature.dem.sweb.service.metaField.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 元字段测试数据查询参数请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class QueryTestDataReq {

    @NotBlank(message = "交易号不能为空")
    @Schema(description = "交易号", example = "GP000001")
    private String txnId;

    @NotBlank(message = "接入点编码不能为空")
    @Schema(description = "接入点编码", example = "A00001")
    private String pointCode;
}
