package com.ly.ttd.language.srv.impl.aviator.fun.system;

import com.googlecode.aviator.runtime.function.system.StrFunction;
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
public class TtdStrFunction extends StrFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674976L;

    public TtdStrFunction() {
        super();
    }

    @MethodName(prefix = "system", method = "str",
            paramType = {"任意类型"},
            desc = "转字符串",
            returnObj = "字符串")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        return super.call(env, arg1);
    }

    public String getName() {
        return "str";
    }
}
