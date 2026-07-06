package com.ly.ttd.biz.admin.srv.factor.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 实时特征指标查询响应
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class FeatureFactorDetailRes extends FactorDetailRes {
    @Schema(description = "实时特征编码")
    private String featureCode;
}
