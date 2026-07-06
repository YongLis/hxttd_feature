package com.ly.ttd.feature.common.model;

import lombok.Data;

import java.util.List;

/**
 * 接入点实体
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
public class AccessPointModel {
    /**
     * 接入点编码
     */
    private String code;

    /**
     * 接入点名称
     */
    private String name;

    /**
     * 接入点描述
     */
    private String description;

    /**
     * 所属项目ID
     */
    private Long projectId;

    /**
     * 资源版本号
     */
    private String version;


    /**
     * 状态(ENABLED/DISABLED)
     */
    private String status;

    /**
     * 操作类型（QUERY/STORAGE）
     */
    private String operationType;

    /**
     * 下级模型
     */
    private List<String> subModels;


}
