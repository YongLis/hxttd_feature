package com.ly.ttd.language.srv.impl.aviator.fun.math;

import com.googlecode.aviator.runtime.function.math.MathPowFunction;
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
public class TtdMathPowFunction extends MathPowFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674968L;

    public TtdMathPowFunction() {
        super();
    }

    @MethodName(prefix = "math", method = "pow",
            paramType = {"底数(小数｜整数)", "指数(小数｜整数)"},
            desc = "返回底数的指数次幂",
            returnObj = "小数｜整数")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        return super.call(env, arg1, arg2);
    }

    public String getName() {
        return "math.pow";
    }
}
