package com.ly.ttd.language.srv.impl.aviator.fun.math;

import com.googlecode.aviator.runtime.function.math.MathSinFunction;
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
public class TtdMathSinFunction extends MathSinFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674956L;

    public TtdMathSinFunction() {
        super();
    }

    @MethodName(prefix = "math", method = "sin",
            paramType = {"角度值"},
            desc = "返回角度的正弦值",
            returnObj = "小数")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        return super.call(env, arg1);
    }

    public String getName() {
        return "math.sin";
    }
}
