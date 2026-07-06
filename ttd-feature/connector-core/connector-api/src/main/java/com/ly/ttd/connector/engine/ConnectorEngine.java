package com.ly.ttd.connector.engine;

import com.ly.ttd.connector.ConnectorException;
import com.ly.ttd.connector.api.AbstractConnector;
import com.ly.ttd.connector.api.AbstractConnectorRequest;
import com.ly.ttd.connector.api.ConnectorResponse;
import com.ly.ttd.connector.api.spi.Connector;
import com.ly.ttd.connector.api.spi.ConnectorObserver;
import com.ly.ttd.connector.engine.cache.ConnectorCache;
import com.ly.ttd.connector.engine.filter.ConnectorFilter;
import com.ly.ttd.connector.engine.mapper.OutputMapper;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 连接器执行引擎
 * 负责管理连接器实例、执行调用、缓存结果
 *
 * @author yong.li
 * @since 2026/04/15
 */
public class ConnectorEngine {

    private static final Logger logger = LoggerFactory.getLogger(ConnectorEngine.class);

    /**
     * 连接器注册表
     */
    private final Map<String, Connector<?, ?>> connectorRegistry = new ConcurrentHashMap<>();

    /**
     * 默认线程池
     */
    private ThreadPoolExecutor defaultExecutor;

    /**
     * 过滤器（可选）
     */
    private ConnectorFilter filter;

    /**
     * 缓存管理器（可选）
     */
    private ConnectorCache cache;

    /**
     * 输出映射器（可选）
     */
    private OutputMapper outputMapper;

    /**
     * 注册连接器
     *
     * @param connectorId 连接器ID
     * @param connector   连接器实例
     */
    public void registerConnector(String connectorId, Connector<?, ?> connector) {
        connectorRegistry.put(connectorId, connector);
        logger.info("Registered connector: {}", connectorId);
    }

    /**
     * 获取连接器
     *
     * @param connectorId 连接器ID
     * @return 连接器实例
     */
    @SuppressWarnings("unchecked")
    public <K extends AbstractConnectorRequest, V extends ConnectorResponse> Connector<K, V> getConnector(String connectorId) {
        Connector<?, ?> connector = connectorRegistry.get(connectorId);
        if (connector == null) {
            throw new ConnectorException("CONNECTOR_NOT_FOUND", "连接器未找到: " + connectorId);
        }
        return (Connector<K, V>) connector;
    }

    /**
     * 同步执行连接器
     *
     * @param connectorId 连接器ID
     * @param request     请求对象
     * @param observers   观察者列表
     * @return 响应结果
     */
    @SuppressWarnings("unchecked")
    public <K extends AbstractConnectorRequest, V extends ConnectorResponse> V executeSync(
            String connectorId, K request, Collection<ConnectorObserver<K, V>> observers) throws FeatureBizException {

        long startTime = Instant.now().toEpochMilli();

        // 1. 过滤器检查
        if (filter != null && !filter.shouldExecute(request)) {
            logger.info("Connector {} filtered, skip execution", connectorId);
            return null;
        }

        // 2. 缓存检查
        if (cache != null) {
            String cacheKey = cache.generateCacheKey(connectorId, null);
            V cachedResponse = (V) cache.getFromCache(cacheKey);
            if (cachedResponse != null) {
                logger.info("Connector {} cache hit, key={}", connectorId, cacheKey);
                return cachedResponse;
            }
        }

        // 3. 真实调用
        Connector<K, V> connector = getConnector(connectorId);
        V response = connector.execute(request, observers);

        // 4. 缓存结果
        if (cache != null && response != null) {
            String cacheKey = cache.generateCacheKey(connectorId, null);
            cache.putToCache(cacheKey, response);
        }

        long cost = Instant.now().toEpochMilli() - startTime;

        // 慢调用告警（>2秒）
        if (cost > 2000) {
            logger.warn("Connector execution slow, connectorId={}, cost={}ms", connectorId, cost);
        }

        return response;
    }

    /**
     * 异步执行连接器
     *
     * @param connectorId 连接器ID
     * @param request     请求对象
     * @param observers   观察者列表
     */
    @SuppressWarnings("unchecked")
    public <K extends AbstractConnectorRequest, V extends ConnectorResponse> void executeAsync(
            String connectorId, K request, Collection<ConnectorObserver<K, V>> observers) {

        Connector<K, V> connector = getConnector(connectorId);
        connector.executeAsync(request, defaultExecutor, observers);
    }

    /**
     * 异步执行连接器（带返回值）
     *
     * @param connectorId 连接器ID
     * @param request     请求对象
     * @param observers   观察者列表
     * @return Future对象
     */
    @SuppressWarnings("unchecked")
    public <K extends AbstractConnectorRequest, V extends ConnectorResponse> CompletableFuture<V> executeAsyncWithReturn(
            String connectorId, K request, Collection<ConnectorObserver<K, V>> observers) {

        Connector<K, V> connector = getConnector(connectorId);
        return connector.executeAsyncWithReturn(request, defaultExecutor, observers);
    }

    /**
     * 设置默认线程池
     *
     * @param executor 线程池
     */
    public void setDefaultExecutor(ThreadPoolExecutor executor) {
        this.defaultExecutor = executor;
    }

    /**
     * 获取所有已注册的连接器ID
     *
     * @return 连接器ID集合
     */
    public Map<String, Connector<?, ?>> getConnectorRegistry() {
        return connectorRegistry;
    }

    /**
     * 设置过滤器
     *
     * @param filter 过滤器
     */
    public void setFilter(ConnectorFilter filter) {
        this.filter = filter;
    }

    /**
     * 设置缓存管理器
     *
     * @param cache 缓存管理器
     */
    public void setCache(ConnectorCache cache) {
        this.cache = cache;
    }

    /**
     * 设置输出映射器
     *
     * @param outputMapper 输出映射器
     */
    public void setOutputMapper(OutputMapper outputMapper) {
        this.outputMapper = outputMapper;
    }

    /**
     * 应用输出映射
     *
     * @param response   响应数据
     * @param expression 映射表达式
     * @return 映射后的结果
     */
    public Object applyOutputMapping(Map<String, Object> response, String expression) {
        if (outputMapper == null) {
            return response;
        }
        return outputMapper.applyMapping(response, expression);
    }

    /**
     * 销毁所有连接器
     */
    public void destroy() {
        connectorRegistry.forEach((id, connector) -> {
            try {
                if (connector instanceof AbstractConnector) {
                    ((AbstractConnector<?, ?>) connector).destroy();
                }
                logger.info("Destroyed connector: {}", id);
            } catch (Exception e) {
                logger.warn("Failed to destroy connector: {}", id, e);
            }
        });
        connectorRegistry.clear();
    }
}
