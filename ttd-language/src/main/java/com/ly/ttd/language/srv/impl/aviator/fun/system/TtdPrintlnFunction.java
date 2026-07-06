package com.ly.ttd.language.srv.impl.aviator.fun.system;

import com.googlecode.aviator.runtime.function.system.PrintlnFunction;
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
public class TtdPrintlnFunction extends PrintlnFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674975L;

    public TtdPrintlnFunction() {
        super();
    }

    @MethodName(prefix = "system", method = "println",
            paramType = {"任意类型"},
            desc = "打印任意类型的值并换行",
            returnObj = "任意类型")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        return super.call(env, arg1);
    }

    public String getName() {
        return "println";
    }
}
