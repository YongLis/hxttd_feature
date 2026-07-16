package com.ly.ttd.biz.feature.dem.sweb.controller.system;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.base.result.BaseRequest;
import com.ly.ttd.base.result.Result;
import com.ly.ttd.biz.feature.dem.sweb.service.user.UserAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.user.req.UserLoginReq;
import com.ly.ttd.biz.feature.dem.sweb.service.user.res.UserLoginRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员 Controller
 * 提供登录、登出、获取当前用户等接口
 *
 * @author yong.li
 * @since 2026-05-16
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "认证授权", description = "用户登录、登出、会话管理接口")
public class AdminController {

    @Resource
    private UserAdminService userService;

    @Operation(summary = "用户登录", description = "通过用户名密码登录，返回 sessionId 和用户信息")
    @PostMapping("/login")
    public Result<UserLoginRes> login(@RequestBody UserLoginReq req) {
        try {
            UserLoginRes res = userService.login(req);
            return Result.success(res);
        } catch (BizException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }

    @Operation(summary = "用户登出", description = "清除会话，退出登录")
    @PostMapping("/logout")
    public Result<Boolean> logout(HttpServletRequest request) {
        String sessionId = request.getHeader("X-Session-Id");

        BaseRequest req = new BaseRequest();
        req.setSessionId(sessionId);

        Boolean success = userService.logout(req);
        return Result.success(success);
    }

    @Operation(summary = "获取当前登录用户信息", description = "从会话中获取当前登录用户的完整信息")
    @PostMapping("/getCurrentUser")
    public Result<UserLoginRes> getCurrentUser() {
        try {
            UserLoginRes res = userService.getCurrentUser();
            return Result.success(res);
        } catch (BizException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }
}
