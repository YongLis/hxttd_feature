package com.ly.ttd.language.srv.impl.groovy;

import com.ly.ttd.language.srv.impl.groovy.fun.GroovyFun;
import groovy.lang.Binding;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * groovy函数注册器
 *
 * @author yong.li
 * @since 2026/7/1 11:04
 */
@Component
public class GroovyFunctionRegistry {
    @Resource
    private ListableBeanFactory beanFactory;

    private final Map<String, GroovyFun> functionMap = new HashMap<>();

    @PostConstruct
    public void init() {
        // 扫描所有 GroovyFun 的实现类
        Map<String, GroovyFun> beans = beanFactory.getBeansOfType(GroovyFun.class);
        for (GroovyFun fun : beans.values()) {
            String name = fun.getFunName();
            if (functionMap.containsKey(name)) {
                throw new IllegalStateException("函数名重复: " + name);
            }
            functionMap.put(name, fun);
        }
    }

    /**
     * 将注册的函数注入到 Binding 中
     */
    public Binding enrichBinding(Binding binding) {
        for (Map.Entry<String, GroovyFun> entry : functionMap.entrySet()) {
            // 核心：将函数名作为变量名，GroovyFun 实例作为值
            // 配合 Groovy 的 metaClass 实现函数式调用
            GroovyFun fun = entry.getValue();
            binding.setVariable(entry.getKey(), fun);
        }
        return binding;
    }

    /**
     * 获取所有已注册的函数名（用于调试）
     */
    public Map<String, GroovyFun> getFunctionMap() {
        return functionMap;
    }
}
