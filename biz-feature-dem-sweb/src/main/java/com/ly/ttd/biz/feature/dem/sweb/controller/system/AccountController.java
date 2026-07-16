package com.ly.ttd.biz.feature.dem.sweb.controller.system;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.base.result.Result;
import com.ly.ttd.biz.feature.dem.sweb.service.account.AccountQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.account.UserAccountAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.account.req.AccountAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.account.req.AccountQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.account.res.AccountQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.service.user.req.ResetPasswordReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 账户管理 Controller
 *
 * @author yong.li
 * @since 2026-05-16
 */
@RestController
@RequestMapping("/api/account")
@Tag(name = "账户管理", description = "平台用户账户 CRUD 管理接口")
@Slf4j
public class AccountController {

    @Resource
    private AccountQueryService accountQueryService;

    @Resource
    private UserAccountAdminService userAccountAdminService;

    @Operation(summary = "分页查询账户", description = "根据条件分页查询账户列表")
    @PostMapping("/page")
    public PageResult<AccountQueryRes> page(@RequestBody AccountQueryReq req) {
        return accountQueryService.pageQuery(req);
    }

    @Operation(summary = "添加账户")
    @PostMapping("/add")
    public Result<Boolean> add(@Valid @RequestBody AccountAddReq req) {
        try {
            userAccountAdminService.addAccount(req);
            return Result.success(true);
        } catch (BizException e) {
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e2) {
            log.error("add account error", e2);
            return Result.error(e2.getMessage());
        }

    }

    @Operation(summary = "删除账户")
    @PostMapping("/delete")
    public Result<Boolean> delete(@Parameter(description = "账户ID") @RequestParam Long id) {
        try {
            userAccountAdminService.deleteAccount(id);
            return Result.success(true);
        } catch (BizException e) {
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e2) {
            log.error("delete account error", e2);
            return Result.error(e2.getMessage());
        }
    }

    @Operation(summary = "重置密码")
    @PostMapping("/resetPassword")
    public Result<Boolean> resetPassword(@Valid @RequestBody ResetPasswordReq req) {


        try {
            userAccountAdminService.resetPassword(req);
            return Result.success(true);
        } catch (BizException e) {
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e2) {
            log.error("reset password error", e2);
            return Result.error(e2.getMessage());
        }
    }
}
