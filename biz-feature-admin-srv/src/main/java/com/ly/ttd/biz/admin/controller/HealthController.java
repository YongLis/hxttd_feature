package com.ly.ttd.biz.admin.controller;

import com.ly.ttd.biz.admin.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author yong.li
 * @since 2026/5/6 11:14
 */
@RestController
@RequestMapping("/api/health")
@Tag(name = "健康检查", description = "服务健康状态检测接口")
public class HealthController {

    @Operation(summary = "健康检查", description = "检测服务是否正常运行")
    @RequestMapping("/check")
    public Result<Boolean> check() {
        return Result.success(true);
    }
}
