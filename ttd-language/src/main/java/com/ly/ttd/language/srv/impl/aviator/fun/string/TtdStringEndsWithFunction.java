package com.ly.ttd.language.srv.impl.aviator.fun.string;

import com.googlecode.aviator.runtime.function.string.StringEndsWithFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.ly.ttd.language.srv.impl.aviator.fun.TtdAviatorDefineFun;
import com.ly.ttd.language.srv.tip.FunctionDef;
import com.ly.ttd.language.srv.tip.MethodName;

import java.util.Map;

/**
 * @author yong.li
 * @since 2026/1/26 11:49
 */
@FunctionDef
public class TtdStringEndsWithFunction extends StringEndsWithFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674980L;

    public TtdStringEndsWithFunction() {
        super();
    }

    @MethodName(prefix = "string", method = "ends_with",
            paramType = {"字符串", "后缀"},
            desc = "字符串是否以指定的字符串结尾",
            returnObj = "布尔值")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        return super.call(env, arg1, arg2);
    }

    public String getName() {
        return "string.ends_with";
    }
}
