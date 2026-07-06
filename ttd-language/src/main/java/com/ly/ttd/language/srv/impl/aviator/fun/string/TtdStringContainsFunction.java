package com.ly.ttd.language.srv.impl.aviator.fun.string;

import com.googlecode.aviator.runtime.function.string.StringContainsFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.ly.ttd.feature.common.tip.FunctionDef;
import com.ly.ttd.feature.common.tip.MethodName;
import com.ly.ttd.language.srv.impl.aviator.fun.TtdAviatorDefineFun;

import java.util.Map;

/**
 * @author yong.li
 * @since 2026/1/26 11:49
 */
@FunctionDef
public class TtdStringContainsFunction extends StringContainsFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674978L;

    public TtdStringContainsFunction() {
        super();
    }

    @MethodName(prefix = "string", method = "contains",
            paramType = {"字符串", "子串"},
            desc = "字符串包含",
            returnObj = "布尔值")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        return super.call(env, arg1, arg2);
    }

    public String getName() {
        return "string.contains";
    }
}
