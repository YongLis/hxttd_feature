package com.ly.ttd.nacos.consts;

/**
 * 默认nacos配置枚举
 *
 * @author yong.li
 * @since 2026/3/10 19:22
 */
public enum DefaultNacosConfigEnum {
    REDIS("redis.properties"),
    KAFKA("kafka.properties"),
    ROCKETMQ("rocketmq.properties"),
    MYSQL("mysql.properties"),

    /**
     * 应用的namespaceId为服务名称，app不启用
     */
    APP("application.properties");

    private final String dataId;

    DefaultNacosConfigEnum(String dataId) {
        this.dataId = dataId;
    }

    public String getDataId() {
        return dataId;
    }

}
