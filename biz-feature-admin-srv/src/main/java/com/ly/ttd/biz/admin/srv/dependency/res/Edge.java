package com.ly.ttd.biz.admin.srv.dependency.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 边
 *
 * @author yong.li
 * @since 2026/7/1 14:56
 */
@Data
public class Edge {
    @Schema(description = "边ID")
    private String edgeId;
    @Schema(description = "来源节点")
    private String from;
    @Schema(description = "目标节点")
    private String to;

    public Edge(String edgeId, String from, String to) {
        this.edgeId = edgeId;
        this.from = from;
        this.to = to;
    }
}
