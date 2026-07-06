package com.ly.ttd.connector.engine.cache;

import com.ly.ttd.connector.api.ConnectorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 连接器缓存管理器
 * 支持缓存连接器调用结果，提升性能
 * 支持配置模式，允许开启或关闭缓存
 *
 * @author yong.li
 * @since 2026/04/15
 */
public class ConnectorCache {

    private static final Logger logger = LoggerFactory.getLogger(ConnectorCache.class);

    /**
     * 缓存配置
     */
    private final ConnectorCacheConfig config;

    /**
     * 缓存存储
     * Key: 缓存键（connectorCode + 参数hash）
     * Value: 缓存项
     */
    private final Map<String, CacheItem> cache = new ConcurrentHashMap<>();

    /**
     * 构造函数（使用默认配置，默认关闭）
     */
    public ConnectorCache() {
        this(ConnectorCacheConfig.DEFAULT);
    }

    /**
     * 构造函数（使用自定义配置）
     *
     * @param config 缓存配置
     */
    public ConnectorCache(ConnectorCacheConfig config) {
        this.config = config != null ? config : ConnectorCacheConfig.DEFAULT;
        logger.info("ConnectorCache initialized, config: {}", this.config);
    }

    /**
     * 从缓存获取结果
     *
     * @param cacheKey 缓存键
     * @return 缓存的响应，如果不存在或已过期则返回 null
     */
    public ConnectorResponse getFromCache(String cacheKey) {
        if (!config.isEnabled()) {
            logger.debug("Cache is disabled, skip getFromCache for key: {}", cacheKey);
            return null;
        }

        CacheItem item = cache.get(cacheKey);
        if (item == null) {
            logger.debug("Cache miss for key: {}", cacheKey);
            return null;
        }

        // 检查是否过期
        if (item.isExpired()) {
            cache.remove(cacheKey);
            logger.debug("Cache expired for key: {}", cacheKey);
            return null;
        }

        logger.debug("Cache hit for key: {}", cacheKey);
        return item.getResponse();
    }

    /**
     * 设置缓存
     *
     * @param cacheKey 缓存键
     * @param response 响应对象
     * @param ttl      缓存时间（毫秒）
     */
    public void putToCache(String cacheKey, ConnectorResponse response, long ttl) {
        if (!config.isEnabled()) {
            logger.debug("Cache is disabled, skip putToCache for key: {}", cacheKey);
            return;
        }

        if (response == null) {
            logger.warn("Attempt to cache null response for key: {}", cacheKey);
            return;
        }

        // 检查缓存大小限制
        if (cache.size() >= config.getMaxCacheSize()) {
            logger.warn("Cache size exceeded max limit: {}, skip caching for key: {}",
                    config.getMaxCacheSize(), cacheKey);
            return;
        }

        cache.put(cacheKey, new CacheItem(response, ttl));
        logger.debug("Cached response for key: {}, ttl: {}ms", cacheKey, ttl);
    }

    /**
     * 设置缓存（使用配置中的默认TTL）
     *
     * @param cacheKey 缓存键
     * @param response 响应对象
     */
    public void putToCache(String cacheKey, ConnectorResponse response) {
        putToCache(cacheKey, response, config.getDefaultTtl());
    }

    /**
     * 生成缓存键
     *
     * @param connectorCode 连接器编码
     * @param params        请求参数
     * @return 缓存键
     */
    public String generateCacheKey(String connectorCode, Map<String, Object> params) {
        String baseKey;
        if (params == null || params.isEmpty()) {
            baseKey = connectorCode;
        } else {
            baseKey = connectorCode + ":" + params.hashCode();
        }
        return config.getKeyPrefix() + baseKey;
    }

    /**
     * 清除指定缓存
     *
     * @param cacheKey 缓存键
     */
    public void invalidate(String cacheKey) {
        cache.remove(cacheKey);
        logger.info("Invalidated cache for key: {}", cacheKey);
    }

    /**
     * 清除所有缓存
     */
    public void invalidateAll() {
        cache.clear();
        logger.info("Cleared all caches");
    }

    /**
     * 获取缓存配置
     *
     * @return 缓存配置
     */
    public ConnectorCacheConfig getConfig() {
        return config;
    }

    /**
     * 获取缓存大小
     *
     * @return 缓存项数量
     */
    public int size() {
        return cache.size();
    }

    /**
     * 检查缓存是否启用
     *
     * @return true=启用，false=关闭
     */
    public boolean isEnabled() {
        return config.isEnabled();
    }

    /**
     * 缓存项
     */
    private static class CacheItem {
        private final ConnectorResponse response;
        private final long expireTime;

        public CacheItem(ConnectorResponse response, long ttl) {
            this.response = response;
            this.expireTime = System.currentTimeMillis() + ttl;
        }

        public ConnectorResponse getResponse() {
            return response;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }
}
