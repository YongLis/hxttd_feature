package com.ly.ttd.language.srv.impl.aviator.fun.system;

import com.googlecode.aviator.runtime.function.system.NowFunction;
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
public class TtdNowFunction extends NowFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674972L;

    public TtdNowFunction() {
        super();
    }

    @MethodName(prefix = "system", method = "now",
            paramType = {},
            desc = "返回当前日期时间",
            returnObj = "日期")
    public AviatorObject call(Map<String, Object> env) {
        return super.call(env);
    }

    public String getName() {
        return "now";
    }
}
