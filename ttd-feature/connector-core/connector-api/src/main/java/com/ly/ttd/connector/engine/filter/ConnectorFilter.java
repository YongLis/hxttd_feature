package com.ly.ttd.connector.engine.filter;

import com.ly.ttd.connector.api.AbstractConnectorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * 连接器过滤器
 * 用于在执行前判断是否需要执行真实调用
 *
 * @author yong.li
 * @since 2026/04/15
 */
public class ConnectorFilter {

    private static final Logger logger = LoggerFactory.getLogger(ConnectorFilter.class);

    /**
     * 过滤器注册表
     * Key: 连接器编码
     * Value: 过滤条件
     */
    private final Map<String, Predicate<AbstractConnectorRequest>> filters = new ConcurrentHashMap<>();

    /**
     * 注册过滤器
     *
     * @param connectorCode 连接器编码
     * @param predicate     过滤条件（返回 true 表示执行，false 表示跳过）
     */
    public void registerFilter(String connectorCode, Predicate<AbstractConnectorRequest> predicate) {
        filters.put(connectorCode, predicate);
        logger.info("Registered filter for connector: {}", connectorCode);
    }

    /**
     * 判断是否应该执行
     *
     * @param request 请求对象
     * @return true=执行，false=跳过
     */
    public boolean shouldExecute(AbstractConnectorRequest request) {
        String connectorId = request.getConnector().getConnectorId();

        Predicate<AbstractConnectorRequest> predicate = filters.get(connectorId);
        if (predicate == null) {
            // 没有注册过滤器，默认执行
            return true;
        }

        try {
            boolean shouldExecute = predicate.test(request);
            logger.debug("Connector {} filter result: {}", connectorId, shouldExecute);
            return shouldExecute;
        } catch (Exception e) {
            logger.warn("Filter execution failed for connector {}, default to execute", connectorId, e);
            return true; // 过滤器异常时，默认执行
        }
    }

    /**
     * 移除过滤器
     *
     * @param connectorCode 连接器编码
     */
    public void removeFilter(String connectorCode) {
        filters.remove(connectorCode);
        logger.info("Removed filter for connector: {}", connectorCode);
    }

    /**
     * 清除所有过滤器
     */
    public void clear() {
        filters.clear();
    }
}
