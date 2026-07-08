package com.ly.ttd.feature.srv.fallback;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 超时降级处理
 *
 * @author yong.li
 * @since 2026/6/10 09:40
 */
public class FallBackExecutor {

    /**
     * 执行一个任务，超时后返回默认值
     *
     * @param supplier     要执行的任务
     * @param timeout      超时时间
     * @param unit         时间单位
     * @param defaultValue 超时后的默认返回值
     * @param <T>          结果类型
     * @return 任务结果或默认值
     */
    public static <T> T getWithTimeout(Supplier<T> supplier, ThreadPoolExecutor executor, long timeout, TimeUnit unit, T defaultValue, T exceptionValue) {
        try {

            T res = null != executor ?
                    CompletableFuture.supplyAsync(supplier, executor)
                            .orTimeout(timeout, unit)
                            .join()
                    :
                    CompletableFuture
                            .supplyAsync(supplier)
                            .orTimeout(timeout, unit)
                            .join();

            return null == res
                    || (res instanceof String && StringUtils.isEmpty((String) res)) ? defaultValue : res;
        } catch (Exception e) {
            return exceptionValue;
        }
    }
}
