package com.ly.ttd.language.srv.impl.aviator.fun.seq;

import com.googlecode.aviator.runtime.function.seq.SeqContainsKeyFunction;
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
public class TtdSeqContainsKeyFunction extends SeqContainsKeyFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940675002L;

    public TtdSeqContainsKeyFunction() {
    }

    @MethodName(prefix = "seq", method = "contains_key",
            desc = "检查映射中是否包含指定的键",
            paramType = {"映射", "键"},
            returnObj = "布尔值")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        return super.call(env, arg1, arg2);
    }

    public String getName() {
        return "seq.contains_key";
    }
}
