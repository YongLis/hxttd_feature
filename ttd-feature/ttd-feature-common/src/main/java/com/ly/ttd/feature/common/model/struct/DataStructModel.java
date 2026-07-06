package com.ly.ttd.feature.common.model.struct;

import lombok.Data;

import java.util.List;

/**
 * 数据集实体
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class DataStructModel {

    /**
     * 资源唯一标识键
     */
    private String resourceKey;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源版本号
     */
    private String version;

    /**
     * 字段列表
     */
    private List<FieldModel> fieldModels;


}
