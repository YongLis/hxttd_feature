package com.ly.ttd.language.srv.impl.aviator.fun.math;

import com.googlecode.aviator.runtime.function.math.MathCosFunction;
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
public class TtdMathCosFunction extends MathCosFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674957L;

    public TtdMathCosFunction() {
        super();
    }

    @MethodName(prefix = "math", method = "cos",
            paramType = {"角度（弧度）"},
            desc = "返回参数的余弦值",
            returnObj = "小数")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        return super.call(env, arg1);
    }

    public String getName() {
        return "math.cos";
    }
}
