package com.ly.ttd.language.srv.impl;


import com.ly.ttd.language.srv.tip.FunctionTip;

import java.util.List;
import java.util.Map;

/**
 * 抽象引擎执行类，定义了引擎执行的基本方法
 *
 * @author yong.li
 * @since 2026/1/23 14:31
 */
public abstract class AbstractLanguageEngine {

    public abstract String getEngineName();

    /**
     * 执行引擎脚本
     *
     * @param script    引擎脚本
     * @param variables 变量
     * @return 执行结果
     */
    public abstract Object execute(String script, Map<String, Object> variables);

    /**
     * 获取引擎内置函数提示
     */
    public abstract List<FunctionTip> getEngineBuiltinFunctionTips() throws Exception;
}
