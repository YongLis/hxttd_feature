package com.ly.ttd.feature.common.model;

import lombok.Data;

/**
 * @author yong.li
 * @since 2026/5/23 19:51
 */
@Data
public class BaseResourceModel {
    /**
     * 所属项目编码
     */
    private Long projectId;

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
     * 返回值类型(BOOLEAN/STRING/NUMBER)
     */
    private String returnType;

    /**
     * 超时时间（毫秒）
     */
    private Long timeout;


    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 异常值
     */
    private String exceptionValue;

}
