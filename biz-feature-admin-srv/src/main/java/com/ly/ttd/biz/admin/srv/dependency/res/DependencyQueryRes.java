package com.ly.ttd.biz.admin.srv.dependency.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yong.li
 * @since 2026/7/1 14:57
 */
@Data
public class DependencyQueryRes {
    @Schema(description = "节点")
    private List<Node> nodes = new ArrayList<>();
    @Schema(description = "边")
    private List<Edge> edges = new ArrayList<>();


}
