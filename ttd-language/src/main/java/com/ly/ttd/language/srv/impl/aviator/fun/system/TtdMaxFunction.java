package com.ly.ttd.language.srv.impl.aviator.fun.system;

import com.googlecode.aviator.runtime.function.system.MaxFunction;
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
public class TtdMaxFunction extends MaxFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674969L;

    public TtdMaxFunction() {
        super();
    }

    @MethodName(prefix = "system", method = "max",
            paramType = {"数字", "数字"},
            desc = "返回两个数字中的较大值",
            returnObj = "数字")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        return super.call(env, arg1, arg2);
    }

    public String getName() {
        return "max";
    }
}
