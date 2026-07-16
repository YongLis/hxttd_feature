package com.ly.ttd.biz.feature.dem.sweb.cache;

import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yong.li
 * @since 2026/4/2 16:02
 */
@Slf4j
public enum UserCache {
    INSTANCE;
    private static final String PREFIX_KEY = "user_token:";
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
    private Cache<String, Object> cache = CaffeineFactory.getInstance(executor, 30, 5000);

    public void put(String key, Object value) {
        log.info("put user token to cache, key: {}, value: {}", PREFIX_KEY + key, JSON.toJSONString(value));
        cache.put(PREFIX_KEY + key, value);
    }

    public Object getIfPresent(String key) {
        return cache.getIfPresent(PREFIX_KEY + key);
    }

    public void delete(String key) {
        cache.invalidate(PREFIX_KEY + key);
    }

}
