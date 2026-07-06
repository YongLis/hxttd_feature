package com.ly.ttd.language.srv.impl.aviator.fun.seq;

import com.googlecode.aviator.runtime.function.seq.SeqIntoFunction;
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
public class TtdSeqIntoFunction extends SeqIntoFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940675010L;

    public TtdSeqIntoFunction() {
    }

    @MethodName(prefix = "seq", method = "into",
            desc = "将第二个序列中的元素添加到第一个序列中",
            paramType = {"序列", "序列"},
            returnObj = "序列")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        return super.call(env, arg1, arg2);
    }

    public String getName() {
        return "seq.into";
    }
}
