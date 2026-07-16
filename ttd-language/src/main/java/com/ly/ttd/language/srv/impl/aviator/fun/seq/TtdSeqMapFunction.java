package com.ly.ttd.language.srv.impl.aviator.fun.seq;

import com.googlecode.aviator.runtime.function.seq.SeqMapFunction;
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
public class TtdSeqMapFunction extends SeqMapFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674997L;

    public TtdSeqMapFunction() {
    }

    @MethodName(prefix = "seq", method = "map",
            desc = "对序列中的每个元素应用指定的函数，返回新的序列",
            paramType = {"序列", "函数"},
            returnObj = "序列")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        return super.call(env, arg1, arg2);
    }

    public String getName() {
        return "seq.map";
    }
}
