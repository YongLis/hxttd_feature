package com.ly.ttd.connector.engine.cache;

/**
 * 连接器缓存配置
 * 支持通过配置控制缓存的开启/关闭及相关参数
 *
 * @author yong.li
 * @since 2026/04/15
 */
public class ConnectorCacheConfig {

    /**
     * 默认配置
     */
    public static final ConnectorCacheConfig DEFAULT = new ConnectorCacheConfig();

    /**
     * 是否启用缓存
     */
    private boolean enabled = false; // 默认关闭

    /**
     * 默认缓存时间（毫秒）
     */
    private long defaultTtl = 60000; // 60秒

    /**
     * 最大缓存数量
     */
    private int maxCacheSize = 10000;

    /**
     * 缓存键前缀
     */
    private String keyPrefix = "connector:cache:";

    public ConnectorCacheConfig() {
    }

    public ConnectorCacheConfig(boolean enabled, long defaultTtl, int maxCacheSize) {
        this.enabled = enabled;
        this.defaultTtl = defaultTtl;
        this.maxCacheSize = maxCacheSize;
    }

    /**
     * 创建启用的配置
     *
     * @return 启用的配置
     */
    public static ConnectorCacheConfig enabled() {
        return new ConnectorCacheConfig(true, 60000, 10000);
    }

    /**
     * 创建启用的配置（自定义TTL）
     *
     * @param ttl 缓存时间（毫秒）
     * @return 启用的配置
     */
    public static ConnectorCacheConfig enabled(long ttl) {
        return new ConnectorCacheConfig(true, ttl, 10000);
    }

    /**
     * 创建启用的配置（自定义TTL和最大容量）
     *
     * @param ttl          缓存时间（毫秒）
     * @param maxCacheSize 最大缓存数量
     * @return 启用的配置
     */
    public static ConnectorCacheConfig enabled(long ttl, int maxCacheSize) {
        return new ConnectorCacheConfig(true, ttl, maxCacheSize);
    }

    /**
     * 创建禁用的配置
     *
     * @return 禁用的配置
     */
    public static ConnectorCacheConfig disabled() {
        return new ConnectorCacheConfig(false, 0, 0);
    }

    /**
     * 从 Builder 构建
     *
     * @return 配置对象
     */
    public static Builder builder() {
        return new Builder();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getDefaultTtl() {
        return defaultTtl;
    }

    public void setDefaultTtl(long defaultTtl) {
        this.defaultTtl = defaultTtl;
    }

    public int getMaxCacheSize() {
        return maxCacheSize;
    }

    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    @Override
    public String toString() {
        return "ConnectorCacheConfig{" +
                "enabled=" + enabled +
                ", defaultTtl=" + defaultTtl +
                ", maxCacheSize=" + maxCacheSize +
                ", keyPrefix='" + keyPrefix + '\'' +
                '}';
    }

    /**
     * Builder 模式构建配置
     */
    public static class Builder {
        private boolean enabled = false;
        private long defaultTtl = 60000;
        private int maxCacheSize = 10000;
        private String keyPrefix = "connector:cache:";

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder defaultTtl(long defaultTtl) {
            this.defaultTtl = defaultTtl;
            return this;
        }

        public Builder maxCacheSize(int maxCacheSize) {
            this.maxCacheSize = maxCacheSize;
            return this;
        }

        public Builder keyPrefix(String keyPrefix) {
            this.keyPrefix = keyPrefix;
            return this;
        }

        public ConnectorCacheConfig build() {
            ConnectorCacheConfig config = new ConnectorCacheConfig();
            config.setEnabled(this.enabled);
            config.setDefaultTtl(this.defaultTtl);
            config.setMaxCacheSize(this.maxCacheSize);
            config.setKeyPrefix(this.keyPrefix);
            return config;
        }
    }
}
