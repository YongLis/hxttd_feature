package com.ly.ttd.biz.admin.controller;

import com.ly.ttd.biz.admin.common.Result;
import com.ly.ttd.biz.admin.consts.LoginUserUtils;
import com.ly.ttd.biz.admin.mybatis.entity.ProjectEntity;
import com.ly.ttd.biz.admin.srv.project.ProjectService;
import com.ly.ttd.feature.common.enums.ScriptType;
import com.ly.ttd.feature.common.tip.FunctionTip;
import com.ly.ttd.language.srv.impl.AbstractLanguageEngine;
import com.ly.ttd.language.srv.impl.LanguageExecuteFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统通用 Controller
 *
 * @author yong.li
 * @since 2026-05-16
 */
@RestController
@RequestMapping("/api/system")
@Tag(name = "系统配置", description = "项目信息、脚本提示等系统通用接口")
@Slf4j
public class SystemController {

    @Resource
    private ProjectService projectService;

    @Operation(summary = "获取当前项目信息", description = "根据请求头中 projectId 查询项目详情")
    @PostMapping("/getProject")
    public Result<Map<String, Object>> getProject() {
        Long projectId = LoginUserUtils.INSTANCE.getProjectId();
        if (projectId == null) {
            return Result.error("未选择项目");
        }

        ProjectEntity project = projectService.getById(projectId);
        if (project == null) {
            return Result.error("项目不存在");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("id", project.getId());
        result.put("projectCode", project.getProjectCode());
        result.put("name", project.getName());
        result.put("crtUser", project.getCrtUser());
        result.put("crtTime", project.getCrtTime());

        return Result.success(result);
    }

    @Operation(summary = "获取脚本函数提示", description = "根据语言类型(aviator/groovy)获取内置函数提示列表")
    @GetMapping("/getFunctionTips")
    public Result<List<FunctionTip>> getFunctionTips(@Parameter(description = "脚本语言: aviator 或 groovy") @RequestParam String language) {
        List<FunctionTip> list = new ArrayList<>();

        if (ScriptType.AVIATOR.getCode().equals(language)
                || ScriptType.GROOVY.getCode().equals(language)) {
            try {
                AbstractLanguageEngine engine = LanguageExecuteFactory.getInstance(language);
                if (engine != null) {
                    list.addAll(engine.getEngineBuiltinFunctionTips());
                } else {
                    log.warn("LanguageExecuteFactory.getInstance() returned null for language={}", language);
                }
            } catch (Exception e) {
                log.error("getFunctionTips error, language={}", language, e);
                Result.error("系统异常，请稍后再试");
            }
        }

        return Result.success(list);

    }
}
