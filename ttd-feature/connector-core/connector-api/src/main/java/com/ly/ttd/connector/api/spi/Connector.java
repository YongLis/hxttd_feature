package com.ly.ttd.connector.api.spi;

import com.ly.ttd.connector.api.AbstractConnectorRequest;
import com.ly.ttd.connector.ConnectorException;
import com.ly.ttd.connector.api.ConnectorResponse;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 连接器接口
 * 分三种使用场景：
 * 1. 测试场景，取值直接返回，不做数据处理
 * 2. 生产场景（同步/异步， 执行完毕后触发观察者处理）
 * 3. 生产陪跑场景（同步/异步， 执行完毕根据观察者判断，不为空则做处理）
 *
 * @author yong.li
 * @since 2026/3/27 13:36
 */
public interface Connector<K extends AbstractConnectorRequest, V extends ConnectorResponse> {

    // 连接器ID
    public String getConnectorId();

    /**
     * 获取连接器类型
     */
    public String getConnectorType();

    /**
     * 创建请求
     *
     * @return 请求
     */
    public K createRequest();


    /**
     * 同步执行
     */
    public V execute(K req) throws ConnectorException;

    /**
     * 同步执行
     */
    public V execute(K req, Collection<ConnectorObserver<K, V>> observers) throws ConnectorException;


    /**
     * 同步执行（试运行/测试专用）
     */
    public V execute(K req, ConnectorObserver<K, V> observer) throws ConnectorException;


    /**
     * 异步执行（试运行/测试专用）
     *
     * @param req
     */
    public void executeAsync(K req);


    /**
     * 异步执行
     *
     * @param req
     * @param executor
     * @param observers 观察者
     */
    public void executeAsync(K req, ThreadPoolExecutor executor, Collection<ConnectorObserver<K, V>> observers);


    /**
     * 异步有返回值(试运行/测试专用)
     *
     * @param req 请求
     * @return 异步执行结果
     **/
    public CompletableFuture<V> executeAsyncWithReturn(K req);


    /**
     * 异步有返回值
     *
     * @param req       请求
     * @param executor  线程池执行器
     * @param observers 观察者
     * @return 异步执行结果
     **/
    public CompletableFuture<V> executeAsyncWithReturn(K req, ThreadPoolExecutor executor, Collection<ConnectorObserver<K, V>> observers);


}
