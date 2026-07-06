package com.ly.ttd.connector.api.spi;

import com.ly.ttd.connector.api.ConnectorResponse;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 连接器请求接口
 * 定义连接器的请求规范和执行方法
 *
 * @author yong.li
 * @since 2026/04/15
 */
public interface ConnectorRequest<R extends ConnectorResponse> {

    /**
     * 设置请求参数
     *
     * @param params 参数Map
     */
    void setRequestParameters(Map<String, Object> params);

    /**
     * 设置单个请求参数
     *
     * @param name  参数名
     * @param value 参数值
     */
    void setRequestParameter(String name, Object value);

    /**
     * 获取所有请求参数
     *
     * @return 参数Map
     */
    Map<String, Object> getRequestParameters();

    /**
     * 获取指定参数
     *
     * @param name 参数名
     * @param <V>  参数类型
     * @return 参数值
     */
    <V> V getRequestParameter(String name);

    /**
     * 同步执行
     *
     * @return 响应结果
     */
    R execute();

    /**
     * 异步执行（带监听器）
     *
     * @param observer 监听器
     */
    void execute(ConnectorObserver<?, R> observer);

    /**
     * 异步执行（无返回值）
     */
    void executeAsync();

    /**
     * 异步执行（返回Future）
     *
     * @return Future对象
     */
    CompletableFuture<R> executeFuture();

    /**
     * 设置超时时间
     *
     * @param timeout 超时时间
     */
    void setRequestTimeout(Duration timeout);

    /**
     * 获取超时时间
     *
     * @return 超时时间
     */
    Duration getRequestTimeout();
}
