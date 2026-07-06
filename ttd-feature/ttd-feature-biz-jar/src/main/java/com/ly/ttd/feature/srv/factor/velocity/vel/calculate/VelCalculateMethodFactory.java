package com.ly.ttd.feature.srv.factor.velocity.vel.calculate;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/4/24 18:03
 */
@Component
public class VelCalculateMethodFactory {
    private static final Map<String, AbstractVelCalculateMethod> methodMap = new HashMap<>();

    @Resource
    private ApplicationContext context;

    @PostConstruct
    public void init() {
        Map<String, AbstractVelCalculateMethod> beans = context.getBeansOfType(AbstractVelCalculateMethod.class);
        beans.forEach((key, value) -> methodMap.put(value.getCalculateMethod(), value));
    }

    public static AbstractVelCalculateMethod getMethod(String method) {
        return methodMap.get(method);
    }
}
