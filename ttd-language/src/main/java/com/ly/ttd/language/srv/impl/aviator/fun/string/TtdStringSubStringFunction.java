package com.ly.ttd.language.srv.impl.aviator.fun.string;

import com.googlecode.aviator.runtime.function.string.StringSubStringFunction;
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
public class TtdStringSubStringFunction extends StringSubStringFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674982L;

    public TtdStringSubStringFunction() {
        super();
    }

    @MethodName(prefix = "string", method = "substring",
            paramType = {"字符串", "开始位置", "结束位置"},
            desc = "截取子串",
            returnObj = "字符串")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        return super.call(env, arg1, arg2, arg3);
    }

    @MethodName(
            prefix = "string", method = "substring",
            paramType = {"字符串", "开始位置"},
            desc = "截取子串",
            returnObj = "字符串"
    )
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        return super.call(env, arg1, arg2);
    }

    public String getName() {
        return "string.substring";
    }
}
