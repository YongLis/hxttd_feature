package com.ly.ttd.language.srv.impl.aviator.fun.seq;

import com.googlecode.aviator.runtime.function.seq.SeqGetFunction;
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
public class TtdSeqGetFunction extends SeqGetFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674990L;

    public TtdSeqGetFunction() {
        super();
    }

    @MethodName(prefix = "seq", method = "get",
            desc = "根据索引从序列中获取元素",
            paramType = {"序列", "索引"},
            returnObj = "任意类型")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        return super.call(env, arg1, arg2);
    }

    public String getName() {
        return "seq.get";
    }
}
