package com.ly.ttd.language.srv.impl.aviator.fun.seq;

import com.googlecode.aviator.runtime.function.seq.SeqNewListFunction;
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
public class TtdSeqNewListFunction extends SeqNewListFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674985L;

    public TtdSeqNewListFunction() {
        super();
    }

    @MethodName(prefix = "seq", method = "new_list",
            desc = "创建一个新的列表",
            paramType = {},
            returnObj = "列表")
    public AviatorObject call(Map<String, Object> env) {
        return super.call(env);
    }

    public String getName() {
        return "seq.new_list";
    }
}
