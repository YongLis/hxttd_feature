package com.ly.ttd.language.srv.impl.aviator.fun.string;

import com.googlecode.aviator.runtime.function.string.StringLengthFunction;
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
public class TtdStringLengthFunction extends StringLengthFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674977L;

    public TtdStringLengthFunction() {
        super();
    }

    @MethodName(prefix = "string", method = "length",
            paramType = {"字符串"},
            desc = "获取字符串长度",
            returnObj = "整数")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        return super.call(env, arg1);
    }

    public String getName() {
        return "string.length";
    }
}
