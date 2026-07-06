package com.ly.ttd.biz.admin.srv.factor.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 指标血缘查询响应
 *
 * @author yong.li
 * @since 2026-06-30
 */
@Data
public class FactorDependencyQueryRes {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "所属项目ID")
    private Long projectId;

    /**
     * 父指标（下游，依赖方）
     */
    @Schema(description = "父指标（下游，依赖方）")
    private String parent;

    /**
     * 子指标（上游，被依赖方）
     */
    @Schema(description = "子指标（上游，被依赖方）")
    private String child;
}
