package com.ly.ttd.language.srv.impl.aviator.fun.seq;

import com.googlecode.aviator.runtime.function.seq.SeqSortFunction;
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
public class TtdSeqSortFunction extends SeqSortFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674999L;

    public TtdSeqSortFunction() {
    }

    @MethodName(prefix = "seq", method = "sort",
            desc = "对序列中的元素进行排序",
            paramType = {"序列"},
            returnObj = "序列")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        return super.call(env, arg1);
    }

    public String getName() {
        return "seq.sort";
    }
}
