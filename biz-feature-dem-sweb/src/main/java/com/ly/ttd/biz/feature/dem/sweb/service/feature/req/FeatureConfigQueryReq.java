package com.ly.ttd.biz.feature.dem.sweb.service.feature.req;


import com.ly.ttd.base.result.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 特征配置查询请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class FeatureConfigQueryReq extends PageQuery {

    @Schema(description = "资源标识键(模糊查询)")
    private String resourceKey;

    @Schema(description = "资源名称(模糊查询)")
    private String resourceName;

    @Schema(description = "特征编码", example = "FCT_USER_AGE")
    private String featureCode;

    @Schema(description = "返回值类型: STRING/LONG/DOUBLE/DECIMAL/BOOLEAN/LIST/DATE/DICT", example = "LONG")
    private String returnType;

    @Schema(description = "值类型: FEATURE/META_FIELD/FACTOR", example = "FEATURE")
    private String valueType;
}
