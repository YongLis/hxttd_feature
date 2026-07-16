package com.ly.ttd.language.srv.impl.aviator.fun.math;

import com.googlecode.aviator.runtime.function.math.MathAcosFunction;
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
public class TtdMathAcosFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674961L;


    @MethodName(prefix = "math", method = "acos",
            paramType = {"小数｜整数"},
            desc = "返回参数的反余弦值",
            returnObj = "小数")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        return MathAcosFunction.INSTANCE.call(env, arg1);
    }

    public String getName() {
        return "math.acos";
    }
}
