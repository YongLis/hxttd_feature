package com.ly.ttd.feature.srv.connector.op;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yong.li
 * @since 2026/6/15 13:53
 */
@Component
public class ConnectorOpFactory {

    private static Map<String, AbstractConnectorOpService> serviceMap = new ConcurrentHashMap<>();

    @Resource
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        applicationContext.getBeansOfType(AbstractConnectorOpService.class)
                .values()
                .forEach(t -> {
                    serviceMap.put(t.getConnectorType(), t);
                });
    }

    public static AbstractConnectorOpService getConnectorOpService(String connectorType) {
        return serviceMap.get(connectorType);
    }


}
