package com.ly.ttd.language.srv.impl.aviator.fun.seq;

import com.googlecode.aviator.runtime.function.seq.SeqKeysFunction;
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
public class TtdSeqKeysFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674995L;
    private final SeqKeysFunction delegate = SeqKeysFunction.INSTANCE;

    public TtdSeqKeysFunction() {
    }

    @MethodName(prefix = "seq", method = "keys",
            desc = "获取映射中的所有键",
            paramType = {"映射"},
            returnObj = "键列表")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        return delegate.call(env, arg1);
    }

    public String getName() {
        return "seq.keys";
    }
}
