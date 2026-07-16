package com.ly.ttd.biz.feature.dem.sweb.service.factor.req;


import com.ly.ttd.base.result.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 指标血缘查询请求
 *
 * @author yong.li
 * @since 2026-06-30
 */
@Data
public class FactorDependencyQueryReq extends PageQuery {

    @Schema(description = "项目ID", example = "1")
    private Long projectId;

    /**
     * 指标编码
     */
    @Schema(description = "指标编码", example = "FCT_USER_AGE")
    private String factorCode;

    /**
     * 查询方向：UP-查询上游依赖(当前指标依赖哪些指标), DOWN-查询下游依赖(哪些指标依赖当前指标)
     */
    @Schema(description = "查询方向: UP-查询上游依赖(当前指标依赖哪些指标), DOWN-查询下游依赖(哪些指标依赖当前指标)", example = "UP")
    private String direction;
}
