package com.ly.ttd.feature.srv.factor.velocity.vel.sink;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/4/24 14:19
 */
@Component
public class VelDataSunkFactory {
    protected static final Map<String, AbstractVelDataSunkService> serviceMap = new HashMap<>();

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void init() {
        Map<String, AbstractVelDataSunkService> map = context.getBeansOfType(AbstractVelDataSunkService.class);
        map.forEach((key, value) -> serviceMap.put(value.getTimeMode(), value));
    }

    public static AbstractVelDataSunkService getService(String cumType) {
        return serviceMap.get(cumType);
    }


}
