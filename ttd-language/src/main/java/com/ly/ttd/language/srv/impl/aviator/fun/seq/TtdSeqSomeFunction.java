package com.ly.ttd.language.srv.impl.aviator.fun.seq;

import com.googlecode.aviator.runtime.function.seq.SeqSomeFunction;
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
public class TtdSeqSomeFunction extends SeqSomeFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940675005L;

    public TtdSeqSomeFunction() {
    }

    @MethodName(prefix = "seq", method = "some",
            desc = "检查序列中是否有元素满足指定的函数条件",
            paramType = {"序列", "函数"},
            returnObj = "布尔值")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        return super.call(env, arg1, arg2);
    }

    public String getName() {
        return "seq.some";
    }
}
