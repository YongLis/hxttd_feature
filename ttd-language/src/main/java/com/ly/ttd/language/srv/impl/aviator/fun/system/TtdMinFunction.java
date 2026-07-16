package com.ly.ttd.language.srv.impl.aviator.fun.system;

import com.googlecode.aviator.runtime.function.system.MinFunction;
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
public class TtdMinFunction extends MinFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674970L;

    public TtdMinFunction() {
        super();
    }

    @MethodName(prefix = "system", method = "min",
            paramType = {"数字", "数字"},
            desc = "返回两个数字中的较小值",
            returnObj = "数字")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        return super.call(env, arg1, arg2);
    }

    public String getName() {
        return "min";
    }
}
