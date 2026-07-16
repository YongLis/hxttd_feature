package com.ly.ttd.language.srv.impl.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.math.MathAbsFunction;
import com.ly.ttd.language.srv.FunctionTipsLoader;
import com.ly.ttd.language.srv.consts.ScriptType;
import com.ly.ttd.language.srv.impl.AbstractLanguageEngine;
import com.ly.ttd.language.srv.tip.FunctionTip;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/1/23 15:12
 */
@Service
public class AviatorLanguage extends AbstractLanguageEngine {
    private static String[] funPackage = {
            "com.ly.ttd.language.srv.impl.aviator.fun.math",
            "com.ly.ttd.language.srv.impl.aviator.fun.seq",
            "com.ly.ttd.language.srv.impl.aviator.fun.string",
            "com.ly.ttd.language.srv.impl.aviator.fun.system",
            "com.ly.ttd.language.srv.impl.aviator.fun.list",
            "com.ly.ttd.language.srv.impl.aviator.fun.date",
    };

    @Override
    public String getEngineName() {
        return ScriptType.AVIATOR.getName();
    }

    @Override
    public Object execute(String script, Map<String, Object> variables) {
        try {
            MathAbsFunction mathAbsFunction = new MathAbsFunction();
            AviatorEvaluator.addFunction(mathAbsFunction);
            return AviatorEvaluator.execute(script, variables, true);
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
        person.put("name", "to2m");
        variables.put("person", person);
        Object result = new AviatorLanguage().execute(script, variables);
        System.out.println(result);
    }
}
