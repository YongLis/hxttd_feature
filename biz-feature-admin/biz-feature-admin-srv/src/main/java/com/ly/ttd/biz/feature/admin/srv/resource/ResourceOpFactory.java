package com.ly.ttd.biz.feature.admin.srv.resource;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/6/23 14:08
 */
@Component
public class ResourceOpFactory {
    private static final Map<String, AbstractResourceOpService> serviceMap = new HashMap<>();

    @Resource
    private ApplicationContext context;

    @PostConstruct
    public void init() {
        context.getBeansOfType(AbstractResourceOpService.class)
                .values()
                .forEach(t -> {
                    serviceMap.put(t.getResourceType(), t);
                });
    }

    public static AbstractResourceOpService getService(String resourceType) {
        return serviceMap.get(resourceType);
    }

}
