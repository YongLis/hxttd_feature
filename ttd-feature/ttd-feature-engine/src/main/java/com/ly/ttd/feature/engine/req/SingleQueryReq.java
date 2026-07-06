package com.ly.ttd.feature.engine.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 单个特征查询
 *
 * @author yong.li
 * @since 2026/6/12 14:40
 */
@Data
public class SingleQueryReq extends TxnFeatureReq {
    @NotBlank(message = "指标编码不能为空")
    private String factorCode;


}
