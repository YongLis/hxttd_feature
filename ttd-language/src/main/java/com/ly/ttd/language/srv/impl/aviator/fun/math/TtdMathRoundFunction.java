package com.ly.ttd.language.srv.impl.aviator.fun.math;

import com.googlecode.aviator.runtime.function.math.MathRoundFunction;
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
public class TtdMathRoundFunction extends MathRoundFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674967L;

    public TtdMathRoundFunction() {
        super();
    }

    @MethodName(prefix = "math", method = "round",
            paramType = {"小数｜整数"},
            desc = "返回最近的整数",
            returnObj = "整数")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        return super.call(env, arg1);
    }

    public String getName() {
        return "math.round";
    }
}
