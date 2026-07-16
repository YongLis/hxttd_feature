package com.ly.ttd.language.srv.impl.aviator.fun.seq;

import com.googlecode.aviator.runtime.function.seq.SeqEveryFunction;
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
public class TtdSeqEveryFunction extends SeqEveryFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940675004L;

    public TtdSeqEveryFunction() {
    }

    @MethodName(prefix = "seq", method = "every",
            desc = "检查序列中的所有元素是否都满足指定的函数条件",
            paramType = {"序列", "函数"},
            returnObj = "布尔值")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        return super.call(env, arg1, arg2);
    }

    public String getName() {
        return "seq.every";
    }
}
