package com.ly.ttd.language.srv.impl.aviator.fun.seq;

import com.googlecode.aviator.runtime.function.seq.SeqNewArrayFunction;
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
public class TtdSeqNewArrayFunction extends SeqNewArrayFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674986L;

    public TtdSeqNewArrayFunction() {
        super();
    }

    @MethodName(prefix = "seq", method = "new_array",
            desc = "创建一个新的数组",
            paramType = {},
            returnObj = "数组")
    public AviatorObject call(Map<String, Object> env) {
        return super.call(env);
    }

    public String getName() {
        return "seq.new_array";
    }
}
