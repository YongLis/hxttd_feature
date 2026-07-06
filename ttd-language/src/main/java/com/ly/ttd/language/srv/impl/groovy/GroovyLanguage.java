package com.ly.ttd.language.srv.impl.groovy;

import com.ly.ttd.feature.common.enums.ScriptType;
import com.ly.ttd.feature.common.tip.FunctionTip;
import com.ly.ttd.language.srv.FunctionTipsLoader;
import com.ly.ttd.language.srv.impl.AbstractLanguageEngine;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/1/23 11:02
 */
@Service
public class GroovyLanguage extends AbstractLanguageEngine {
    @Resource
    private GroovyFunctionRegistry registry;


    private static String[] funPackage = {
            "com.ly.ttd.language.srv.impl.groovy.fun"
    };

    @Override
    public String getEngineName() {
        return ScriptType.GROOVY.getCode();
    }

    /**
     * 执行groovy脚本
     *
     * @param script    groovy脚本
     * @param variables 变量
     * @return 执行结果
     */
    public Object execute(String script, Map<String, Object> variables) {
        try {
            if (null == variables) {
                variables = new HashMap<>();
            }
            if (StringUtils.isEmpty(script)) {
                return null;
            }
            Binding binding = registry.enrichBinding(new Binding(variables));
            GroovyShell groovyShell = new GroovyShell();
            Script compiledScript = groovyShell.parse(script);
            compiledScript.setBinding(binding);
            return compiledScript.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FunctionTip> getEngineBuiltinFunctionTips() throws Exception {
        return FunctionTipsLoader.loadFunctionTips(Arrays.asList(funPackage));
    }

    public static void main(String[] args) {
        String script = "if(person.name == 'tom'){\n" +
                "    return true;\n" +
                "}\n" +
                "return false;";
        Map<String, Object> variables = new HashMap<>();
        Map<String, Object> person = new HashMap<>();
        person.put("name", "tom");
        variables.put("person", person);
        Object result = new GroovyLanguage().execute(script, variables);
        System.out.println(result);

    }

}
