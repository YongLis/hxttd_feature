package com.ly.ttd.language.srv.impl.aviator.fun.seq;

import com.googlecode.aviator.runtime.function.seq.SeqArrayFunction;
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
public class TtdSeqArrayFunction extends SeqArrayFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940675009L;

    public TtdSeqArrayFunction() {
    }

    @MethodName(prefix = "seq", method = "array",
            desc = "将序列转换为数组",
            paramType = {"序列"},
            returnObj = "数组")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        return super.call(env, arg1);
    }

    public String getName() {
        return "seq.array";
    }
}
