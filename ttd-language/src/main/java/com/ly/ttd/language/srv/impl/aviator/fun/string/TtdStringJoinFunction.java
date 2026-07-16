package com.ly.ttd.language.srv.impl.aviator.fun.string;

import com.googlecode.aviator.runtime.function.string.StringJoinFunction;
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
public class TtdStringJoinFunction extends StringJoinFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674983L;

    public TtdStringJoinFunction() {
        super();
    }

    @MethodName(prefix = "string", method = "join",
            paramType = {"字符串列表", "分隔符"},
            desc = "拼接字符",
            returnObj = "字符串")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        return super.call(env, arg1);
    }

    @MethodName(prefix = "string", method = "join",
            paramType = {"字符串列表", "分隔符"},
            desc = "用指定的分隔符拼接字符",
            returnObj = "字符串")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        return super.call(env, arg1, arg2);
    }

    public String getName() {
        return "string.join";
    }
}
