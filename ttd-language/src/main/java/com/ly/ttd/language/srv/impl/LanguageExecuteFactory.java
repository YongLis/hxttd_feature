package com.ly.ttd.language.srv.impl;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/4/17 14:32
 */
@Component
@Slf4j
public class LanguageExecuteFactory {

    private static Map<String, AbstractLanguageEngine> serviceMap = new HashMap<>();

    @Resource
    private ApplicationContext context;

    @PostConstruct
    public void init() {
        log.info("init language execute factory");
        context.getBeansOfType(AbstractLanguageEngine.class)
                .values()
                .forEach(t -> {
                    log.info("register language execute engine: {}", t.getEngineName());
                    serviceMap.put(t.getEngineName(), t);
                });
    }

    public static AbstractLanguageEngine getInstance(String lang) {
        return serviceMap.get(lang);
    }
}
