package com.ly.ttd.biz.feature.dem.sweb.service.factor.req;


import com.ly.ttd.base.result.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 指标查询请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class FactorQueryReq extends PageQuery {

    @Schema(description = "资源标识键(模糊查询)")
    private String resourceKey;

    @Schema(description = "资源名称(模糊查询)")
    private String resourceName;

    @Schema(description = "指标类型: META/DERIVATIVE/FEATURE", example = "FEATURE")
    private String factorType;

    @Schema(description = "返回值类型: STRING/LONG/DOUBLE/DECIMAL/BOOLEAN/LIST/DATE/DICT", example = "LONG")
    private String returnType;

    @Schema(description = "资源标识键列表(批量精确查询)")
    private java.util.List<String> resourceKeys;
}
