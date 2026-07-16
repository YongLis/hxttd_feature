package com.ly.ttd.language.srv.impl.jexl;

import com.ly.ttd.language.srv.consts.ScriptType;
import com.ly.ttd.language.srv.impl.AbstractLanguageEngine;
import com.ly.ttd.language.srv.impl.jexl.fun.JexlFunction;
import com.ly.ttd.language.srv.tip.FunctionTip;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlScript;
import org.apache.commons.jexl3.MapContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yong.li
 * @since 2026/1/23 11:02
 */
@Service
public class JexlLanguage extends AbstractLanguageEngine {
    private Map<String, JexlScript> cache = new ConcurrentHashMap<>();

    @Override
    public String getEngineName() {
        return ScriptType.JEXL.getCode();
    }

    /**
     * 执行jexl脚本
     *
     * @param script    groovy脚本
     * @param variables 变量
     * @return 执行结果
     */
    public Object execute(String script, Map<String, Object> variables) {
        try {
            JexlScript jexlScript = cache.computeIfAbsent(script, this::compileScript);
            MapContext context = new MapContext();
            context.set("fn", new JexlFunction());
            variables.keySet().forEach(key -> context.set(key, variables.get(key)));
            return jexlScript.execute(context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JexlScript compileScript(String script) {
        JexlScript jexlScript = new JexlBuilder().cache(2048)
                .create().createScript(script);
        cache.put(script, jexlScript);
        return jexlScript;
    }

    @Override
    public List<FunctionTip> getEngineBuiltinFunctionTips() throws Exception {
        return new ArrayList<>();
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
        Object result = new JexlLanguage().execute(script, variables);
        System.out.println(result);

    }

}
