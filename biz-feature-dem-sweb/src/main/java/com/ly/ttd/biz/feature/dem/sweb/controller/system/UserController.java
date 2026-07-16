package com.ly.ttd.biz.feature.dem.sweb.controller.system;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.base.result.Result;
import com.ly.ttd.biz.feature.dem.sweb.service.user.UserAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.user.res.UserCurrentRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户 Controller
 *
 * @author yong.li
 * @since 2026-05-16
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户信息", description = "当前用户信息查询接口")
public class UserController {

    @Resource
    private UserAdminService userService;

    @Operation(summary = "获取当前登录用户信息", description = "返回当前用户的角色、所属租户、项目列表等信息")
    @PostMapping("/getCurrent")
    public Result<UserCurrentRes> getCurrent() {
        try {
            UserCurrentRes res = userService.getCurrentUserInfo();
            return Result.success(res);
        } catch (BizException e) {
            return Result.error(e.getCode(), e.getMessage());
        }
    }
}
