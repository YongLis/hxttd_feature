package com.ly.ttd.language.srv.impl.aviator.fun.string;

import com.googlecode.aviator.runtime.function.string.StringIndexOfFunction;
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
public class TtdStringIndexOfFunction extends StringIndexOfFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674981L;

    public TtdStringIndexOfFunction() {
        super();
    }

    @MethodName(prefix = "string", method = "index_of",
            paramType = {"字符串", "子串"},
            desc = "获取下标",
            returnObj = "整数")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        return super.call(env, arg1, arg2);
    }

    public String getName() {
        return "string.index_of";
    }
}
