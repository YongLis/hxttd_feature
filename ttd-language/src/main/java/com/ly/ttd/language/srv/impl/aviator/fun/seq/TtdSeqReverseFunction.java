package com.ly.ttd.language.srv.impl.aviator.fun.seq;

import com.googlecode.aviator.runtime.function.seq.SeqReverseFunction;
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
public class TtdSeqReverseFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940675000L;
    private final SeqReverseFunction delegate = SeqReverseFunction.INSTANCE;

    public TtdSeqReverseFunction() {
    }

    @MethodName(prefix = "seq", method = "reverse",
            desc = "反转序列中的元素顺序",
            paramType = {"序列"},
            returnObj = "序列")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        return delegate.call(env, arg1);
    }

    public String getName() {
        return "seq.reverse";
    }
}
