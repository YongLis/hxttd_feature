package com.ly.ttd.feature.admin.api.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 节点
 *
 * @author yong.li
 * @since 2026/7/1 14:55
 */
@Data
public class Node {
    @Schema(description = "节点ID")
    private String nodeId;
    @Schema(description = "节点类型")
    private String nodeType;
    @Schema(description = "节点标签")
    private String label;

    public Node(String nodeId, String nodeType, String label) {
        this.nodeId = nodeId;
        this.nodeType = nodeType;
        this.label = label;
    }
}
