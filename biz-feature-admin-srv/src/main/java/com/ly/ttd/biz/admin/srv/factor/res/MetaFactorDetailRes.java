package com.ly.ttd.biz.admin.srv.factor.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 元字段指标
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class MetaFactorDetailRes extends FactorDetailRes {

    @Schema(description = "元字段")
    private String metaFieldCode;
}
