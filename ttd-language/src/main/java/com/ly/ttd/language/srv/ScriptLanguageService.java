package com.ly.ttd.language.srv;


import com.ly.ttd.language.srv.req.ScriptVariable;

/**
 * @author yong.li
 * @since 2026/4/17 14:28
 */
public interface ScriptLanguageService {


    /**
     * 执行结果
     *
     * @param variable 脚本变量
     */
    Object execute(ScriptVariable variable);
}
