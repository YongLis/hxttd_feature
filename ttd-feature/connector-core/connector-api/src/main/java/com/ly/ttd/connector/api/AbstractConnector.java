package com.ly.ttd.connector.api;

import com.ly.ttd.connector.ConnectorException;
import com.ly.ttd.connector.api.spi.Connector;
import com.ly.ttd.connector.api.spi.ConnectorInterceptor;
import com.ly.ttd.connector.api.spi.ConnectorObserver;
import com.ly.ttd.connector.api.spi.ConnectorRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 连接器抽象基类
 * 提供拦截器链管理和通用执行逻辑
 *
 * @author yong.li
 * @since 2026/04/15
 */
public abstract class AbstractConnector<K extends AbstractConnectorRequest, V extends ConnectorResponse>
        implements Connector<K, V> {

    private String connectorId;

    public AbstractConnector(String connectorId) {
        this.connectorId = connectorId;
    }

    /**
     * 拦截器列表
     */
    private List<ConnectorInterceptor> interceptors = new ArrayList<>();

    /**
     * 获取拦截器列表
     *
     * @return 拦截器列表
     */
    public List<ConnectorInterceptor> getInterceptors() {
        return Collections.unmodifiableList(interceptors);
    }

    /**
     * 添加拦截器
     *
     * @param interceptor 拦截器
     * @return 当前连接器实例
     */
    public AbstractConnector<K, V> addInterceptor(ConnectorInterceptor interceptor) {
        this.interceptors.add(interceptor);
        return this;
    }

    /**
     * 添加多个拦截器
     *
     * @param interceptors 拦截器集合
     * @return 当前连接器实例
     */
    public AbstractConnector<K, V> addInterceptors(Collection<ConnectorInterceptor> interceptors) {
        this.interceptors.addAll(interceptors);
        return this;
    }

    /**
     * 清除所有拦截器
     */
    public void clearInterceptors() {
        this.interceptors.clear();
    }

    @Override
    public V execute(K req, Collection<ConnectorObserver<K, V>> observers) throws ConnectorException {
        try {
            // 执行拦截器链
            V response = executeWithInterceptors(req);

            // 触发观察者
            if (observers != null && !observers.isEmpty()) {
                for (ConnectorObserver<K, V> observer : observers) {
                    observer.onComplete(req, response);
                }
            }

            return response;
        } catch (Exception e) {
            // 触发观察者异常处理
            if (observers != null && !observers.isEmpty()) {
                for (ConnectorObserver<K, V> observer : observers) {
                    observer.onException(req, null);
                }
            }
            throw new ConnectorException("CONNECTOR_ERROR", "连接器执行失败: " + e.getMessage());
        }
    }

    @Override
    public void executeAsync(K req, ThreadPoolExecutor executor, Collection<ConnectorObserver<K, V>> observers) throws ConnectorException {
        CompletableFuture.runAsync(() -> {
            try {
                V response = executeWithInterceptors(req);

                // 触发观察者
                if (observers != null && !observers.isEmpty()) {
                    for (ConnectorObserver<K, V> observer : observers) {
                        observer.onComplete(req, response);
                    }
                }
            } catch (Exception e) {
                // 触发观察者异常处理
                if (observers != null && !observers.isEmpty()) {
                    for (ConnectorObserver<K, V> observer : observers) {
                        observer.onException(req, null);
                    }
                }
            }
        }, executor);
    }

    @Override
    public void executeAsync(K req) {
        CompletableFuture.runAsync(() -> {
            try {
                executeWithInterceptors(req);
            } catch (Exception e) {
                throw new ConnectorException("连接器异步执行失败: " + e.getMessage(), e);
            }
        });
    }

    @Override
    public CompletableFuture<V> executeAsyncWithReturn(K req) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return executeWithInterceptors(req);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<V> executeAsyncWithReturn(K req, ThreadPoolExecutor executor,
                                                       Collection<ConnectorObserver<K, V>> observers) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                V response = executeWithInterceptors(req);

                // 触发观察者
                if (observers != null && !observers.isEmpty()) {
                    for (ConnectorObserver<K, V> observer : observers) {
                        observer.onComplete(req, response);
                    }
                }

                return response;
            } catch (Exception e) {
                // 触发观察者异常处理
                if (observers != null && !observers.isEmpty()) {
                    for (ConnectorObserver<K, V> observer : observers) {
                        observer.onException(req, null);
                    }
                }
                throw new ConnectorException("连接器异步执行失败: " + e.getMessage(), e);
            }
        }, executor);
    }

    /**
     * 执行拦截器链
     *
     * @param req 请求对象
     * @return 响应结果
     * @throws Exception 异常
     */
    private V executeWithInterceptors(K req) throws Exception {
        if (interceptors.isEmpty()) {
            // 无拦截器，直接执行
            return execute(req);
        }

        // 构建拦截器链
        InterceptorChain chain = new InterceptorChain(req, interceptors, 0, this);
        return (V) chain.proceed();
    }

    /**
     * 拦截器链实现
     */
    private static class InterceptorChain {

        private final AbstractConnectorRequest request;
        private final List<ConnectorInterceptor> interceptors;
        private int index;
        private final AbstractConnector<?, ?> connector;

        public InterceptorChain(AbstractConnectorRequest request,
                                List<ConnectorInterceptor> interceptors,
                                int index,
                                AbstractConnector<?, ?> connector) {
            this.request = request;
            this.interceptors = interceptors;
            this.index = index;
            this.connector = connector;
        }

        public ConnectorResponse proceed() throws Exception {
            if (index >= interceptors.size()) {
                // 所有拦截器执行完毕，调用真实连接器
                return ((AbstractConnector<AbstractConnectorRequest, ConnectorResponse>) connector).execute(request);
            }

            // 执行当前拦截器
            ConnectorInterceptor interceptor = interceptors.get(index);
            InterceptorChain nextChain = new InterceptorChain(request, interceptors, index + 1, connector);

            return interceptor.intercept(new ConnectorInvocationImpl(request, nextChain));
        }
    }

    /**
     * 拦截器调用实现
     */
    private static class ConnectorInvocationImpl implements ConnectorInterceptor.ConnectorInvocation {

        private final AbstractConnectorRequest request;
        private final InterceptorChain nextChain;

        public ConnectorInvocationImpl(AbstractConnectorRequest request, InterceptorChain nextChain) {
            this.request = request;
            this.nextChain = nextChain;
        }

        @Override
        public ConnectorRequest<?> getRequest() {
            return (ConnectorRequest<?>) request;
        }

        @Override
        public ConnectorResponse proceed() throws Exception {
            return nextChain.proceed();
        }
    }

    @Override
    public String getConnectorId() {
        return connectorId;
    }

    /**
     * 回收资源（子类可重写）
     */
    public void destroy() {
        interceptors.clear();
    }
}
