package com.ly.ttd.biz.feature.dem.sweb.service.factor.req;

import com.ly.ttd.biz.feature.dem.sweb.req.ResourceReq;
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
public class FeatureFactorAddReq extends ResourceReq {
    @NotBlank(message = "指标类型不能为空")
    @Schema(description = "指标类型: META/DERIVATIVE/FEATURE", example = "FEATURE")
    private String factorType;

    @NotBlank(message = "返回值类型不能为空")
    @Schema(description = "返回值类型: STRING/LONG/DOUBLE/DECIMAL/BOOLEAN/LIST/DATE/DICT", example = "LONG")
    private String returnType;

    @NotBlank(message = "特征编码不能为空")
    @Schema(description = "特征编码")
    private String featureCode;


}
