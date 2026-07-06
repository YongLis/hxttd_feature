package com.ly.ttd.language.srv.impl.aviator.fun.math;

import com.googlecode.aviator.runtime.function.math.MathLog10Function;
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
public class TtdMathLog10Function extends MathLog10Function implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674963L;

    public TtdMathLog10Function() {
        super();
    }

    @MethodName(prefix = "math", method = "log10",
            paramType = {"小数|整数"},
            desc = "返回参数的以10为底的对数",
            returnObj = "小数")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        return super.call(env, arg1);
    }

    public String getName() {
        return "math.log10";
    }
}
