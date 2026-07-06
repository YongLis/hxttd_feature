package com.ly.ttd.language.srv.impl.aviator.fun.seq;

import com.googlecode.aviator.runtime.function.seq.SeqPutFunction;
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
public class TtdSeqPutFunction extends SeqPutFunction implements TtdAviatorDefineFun {
    private static final long serialVersionUID = 9071009020940674994L;

    public TtdSeqPutFunction() {
        super();
    }

    @MethodName(prefix = "seq", method = "put",
            desc = "向映射中添加键值对",
            paramType = {"映射", "键", "值"},
            returnObj = "映射")
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        return super.call(env, arg1, arg2, arg3);
    }

    public String getName() {
        return "seq.put";
    }
}
