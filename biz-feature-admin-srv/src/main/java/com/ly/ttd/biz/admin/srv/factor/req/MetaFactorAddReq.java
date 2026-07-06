package com.ly.ttd.biz.admin.srv.factor.req;

import com.ly.ttd.biz.admin.req.ResourceReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 指标新增请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class MetaFactorAddReq extends ResourceReq {
    @NotBlank(message = "指标类型不能为空")
    @Schema(description = "指标类型: META/DERIVATIVE/FEATURE", example = "FEATURE")
    private String factorType;

    @NotBlank(message = "返回值类型不能为空")
    @Schema(description = "返回值类型: STRING/LONG/DOUBLE/DECIMAL/BOOLEAN/LIST/DATE/DICT", example = "LONG")
    private String returnType;

    @NotBlank(message = "元字段编码不能为空")
    @Schema(description = "元字段编码")
    private String metaFieldCode;


}
