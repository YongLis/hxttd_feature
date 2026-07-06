package com.ly.ttd.language.srv.impl;

import com.ly.ttd.feature.common.language.ScriptVariable;
import com.ly.ttd.language.srv.ScriptLanguageService;
import org.springframework.stereotype.Service;

/**
 * @author yong.li
 * @since 2026/4/17 14:30
 */
@Service
public class EngineLanguageServiceImpl implements ScriptLanguageService {
    @Override
    public Object execute(ScriptVariable variable) {
        return LanguageExecuteFactory.getInstance(variable.getLang()).execute(variable.getScript(), variable.getParams());
    }
}
