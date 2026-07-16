package com.ly.ttd.language.srv.impl.aviator.fun.seq;

import com.googlecode.aviator.runtime.function.seq.SeqRemoveFunction;
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
public class TtdSeqRemoveFunction extends SeqRemoveFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674993L;

    public TtdSeqRemoveFunction() {
        super();
    }

    @MethodName(prefix = "seq", method = "remove",
            desc = "从序列中移除指定的元素",
            paramType = {"序列", "元素"},
            returnObj = "序列")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        return super.call(env, arg1, arg2);
    }

    public String getName() {
        return "seq.remove";
    }
}
