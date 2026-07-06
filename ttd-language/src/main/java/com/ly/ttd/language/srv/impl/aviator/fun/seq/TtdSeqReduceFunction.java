package com.ly.ttd.language.srv.impl.aviator.fun.seq;

import com.googlecode.aviator.runtime.function.seq.SeqReduceFunction;
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
public class TtdSeqReduceFunction extends SeqReduceFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940675003L;

    public TtdSeqReduceFunction() {
    }

    @MethodName(prefix = "seq", method = "reduce",
            desc = "使用指定的函数和初始值对序列进行归约操作",
            paramType = {"序列", "函数", "初始值"},
            returnObj = "任意类型")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        return super.call(env, arg1, arg2, arg3);
    }

    public String getName() {
        return "seq.reduce";
    }
}
