package com.ly.ttd.language.srv.impl.aviator.fun.seq;

import com.googlecode.aviator.runtime.function.seq.SeqNewMapFunction;
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
public class TtdSeqNewMapFunction extends SeqNewMapFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674987L;

    public TtdSeqNewMapFunction() {
        super();
    }

    @MethodName(prefix = "seq", method = "new_map",
            desc = "创建一个新的映射",
            paramType = {},
            returnObj = "映射")
    public AviatorObject call(Map<String, Object> env) {
        return super.call(env);
    }

    public String getName() {
        return "seq.new_map";
    }
}
