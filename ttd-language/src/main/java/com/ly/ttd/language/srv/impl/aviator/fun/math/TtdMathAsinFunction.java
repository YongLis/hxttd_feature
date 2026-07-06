package com.ly.ttd.language.srv.impl.aviator.fun.math;

import com.googlecode.aviator.runtime.function.math.MathAsinFunction;
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
public class TtdMathAsinFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674962L;

    @MethodName(prefix = "math", method = "asin",
            paramType = {"小数"},
            desc = "返回参数的反正弦值",
            returnObj = "角度（弧度）")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        return MathAsinFunction.INSTANCE.call(env, arg1);
    }

    public String getName() {
        return "math.asin";
    }
}
