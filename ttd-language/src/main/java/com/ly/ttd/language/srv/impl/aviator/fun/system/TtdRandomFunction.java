package com.ly.ttd.language.srv.impl.aviator.fun.system;

import com.googlecode.aviator.runtime.function.system.RandomFunction;
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
public class TtdRandomFunction extends RandomFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674971L;

    public TtdRandomFunction() {
        super();
    }

    @MethodName(prefix = "system", method = "random",
            paramType = {},
            desc = "返回0到1随机小数",
            returnObj = "小数")
    public AviatorObject call(Map<String, Object> env) {
        return super.call(env);
    }

    @MethodName(prefix = "system", method = "random",
            paramType = {"数字"},
            desc = "返回一个随机整数，范围为0到指定的数字（不包含指定的数字）",
            returnObj = "整数")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg) {
        return super.call(env, arg);
    }

    public String getName() {
        return "random";
    }
}
