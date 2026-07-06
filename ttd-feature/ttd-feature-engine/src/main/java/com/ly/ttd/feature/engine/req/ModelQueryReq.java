package com.ly.ttd.feature.engine.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 模型查询
 *
 * @author yong.li
 * @since 2026/6/12 14:40
 */
@Data
public class ModelQueryReq extends TxnFeatureReq {
    /**
     * 按模型查询
     */
    @NotBlank(message = "模型编码不能为空")
    private String modelCode;
}
