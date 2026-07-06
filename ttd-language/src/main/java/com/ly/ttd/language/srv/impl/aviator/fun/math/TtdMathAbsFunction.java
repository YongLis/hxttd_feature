package com.ly.ttd.language.srv.impl.aviator.fun.math;

import com.googlecode.aviator.runtime.function.math.MathAbsFunction;
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
public class TtdMathAbsFunction extends MathAbsFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674955L;

    public TtdMathAbsFunction() {
        super();
    }


    @MethodName(prefix = "math", method = "abs",
            paramType = {"整数｜小数｜金额"},
            desc = "返回参数的绝对值",
            returnObj = "整数｜小数｜金额")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        return super.call(env, arg1);
    }

    public String getName() {
        return "math.abs";
    }


}
