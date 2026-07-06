package com.ly.ttd.feature.cfg.config;

import lombok.Data;

/**
 * @author yong.li
 * @since 2026/4/14 12:13
 */
@Data
public class ElasticsearchConfig {

    private String sourceName;

    /**
     * 运行模式：single-mode, cluster-mode
     */
    private String runMode = "single-mode";

    /**
     * 单节点模式下，host和port必填
     */
    private String host;
    /**
     * 单节点模式下，host和port必填
     */
    private Integer port;


    /**
     * 是否开启权限认证
     */
    private boolean enableSecurity = false;


    /**
     * 集群地址
     */
    private String clusterAddress;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 密码
     */
    private String password;


}
