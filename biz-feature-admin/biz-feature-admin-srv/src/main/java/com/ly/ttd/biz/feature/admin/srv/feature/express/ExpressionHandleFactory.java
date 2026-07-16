package com.ly.ttd.biz.feature.admin.srv.feature.express;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/4/9 10:27
 */
@Component
public class ExpressionHandleFactory {
    private static final Map<String, AbstractExpressionService> serviceMap = new HashMap<>();
    @Resource
    private ApplicationContext context;

    @PostConstruct
    public void init() {
        context.getBeansOfType(AbstractExpressionService.class)
                .values().forEach(t -> serviceMap.put(t.getOp(), t));
    }

    public static AbstractExpressionService getInstance(String op) {
        return serviceMap.get(op);
    }


}
