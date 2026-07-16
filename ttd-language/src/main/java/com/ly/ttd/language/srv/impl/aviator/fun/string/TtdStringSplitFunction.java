package com.ly.ttd.language.srv.impl.aviator.fun.string;

import com.googlecode.aviator.runtime.function.string.StringSplitFunction;
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
public class TtdStringSplitFunction extends StringSplitFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674984L;

    public TtdStringSplitFunction() {
        super();
    }

    @MethodName(prefix = "string", method = "split",
            paramType = {"字符串", "分隔符"},
            desc = "按正则表达式将字符串拆分成给定长度的字符串",
            returnObj = "字符串列表")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        return super.call(env, arg1, arg2, arg3);
    }

    @MethodName(prefix = "string", method = "split",
            paramType = {"字符串", "分隔符"},
            desc = "按分隔符拆分字符串",
            returnObj = "字符串列表")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        return super.call(env, arg1, arg2);
    }

    public String getName() {
        return "string.split";
    }
}
