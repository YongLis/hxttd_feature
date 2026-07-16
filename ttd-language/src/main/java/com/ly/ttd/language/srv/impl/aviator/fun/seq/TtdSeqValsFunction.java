package com.ly.ttd.language.srv.impl.aviator.fun.seq;

import com.googlecode.aviator.runtime.function.seq.SeqValsFunction;
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
public class TtdSeqValsFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674996L;
    private final SeqValsFunction delegate = SeqValsFunction.INSTANCE;

    public TtdSeqValsFunction() {
    }

    @MethodName(prefix = "seq", method = "vals",
            desc = "获取映射中的所有值",
            paramType = {"映射"},
            returnObj = "值列表")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        return delegate.call(env, arg1);
    }

    public String getName() {
        return "seq.vals";
    }
}
