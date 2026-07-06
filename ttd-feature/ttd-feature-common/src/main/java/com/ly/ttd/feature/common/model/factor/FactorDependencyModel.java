package com.ly.ttd.feature.common.model.factor;

import lombok.Data;

/**
 * @author yong.li
 * @since 2026/6/30 23:07
 */
@Data
public class FactorDependencyModel {
    private Long projectId;
    private String parent;
    private String child;
}
