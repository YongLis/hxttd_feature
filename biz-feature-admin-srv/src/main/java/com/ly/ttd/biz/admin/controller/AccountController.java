package com.ly.ttd.biz.admin.controller;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.common.Result;
import com.ly.ttd.biz.admin.srv.account.AccountQueryService;
import com.ly.ttd.biz.admin.srv.account.UserAccountService;
import com.ly.ttd.biz.admin.srv.account.req.AccountAddReq;
import com.ly.ttd.biz.admin.srv.account.req.AccountQueryReq;
import com.ly.ttd.biz.admin.srv.account.req.AccountUpdateReq;
import com.ly.ttd.biz.admin.srv.account.res.AccountQueryRes;
import com.ly.ttd.biz.admin.srv.user.req.ResetPasswordReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
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
public class AccountController {

    @Resource
    private AccountQueryService accountQueryService;

    @Resource
    private UserAccountService userAccountService;

    @Operation(summary = "分页查询账户", description = "根据条件分页查询账户列表")
    @PostMapping("/page")
    public PageResult<AccountQueryRes> page(@RequestBody AccountQueryReq req) {
        return accountQueryService.pageQuery(req);
    }

    @Operation(summary = "添加账户")
    @PostMapping("/add")
    public Result<Boolean> add(@Valid @RequestBody AccountAddReq req) {
        boolean success = userAccountService.addAccount(req);
        return Result.success(success);
    }

    @Operation(summary = "更新账户")
    @PostMapping("/update")
    public Result<Boolean> update(@Valid @RequestBody AccountUpdateReq req) {
        boolean success = userAccountService.updateAccount(req);
        return Result.success(success);
    }

    @Operation(summary = "删除账户")
    @PostMapping("/delete")
    public Result<Boolean> delete(@Parameter(description = "账户ID") @RequestParam Long id) {
        boolean success = userAccountService.deleteAccount(id);
        return Result.success(success);
    }

    @Operation(summary = "重置密码")
    @PostMapping("/resetPassword")
    public Result<Boolean> resetPassword(@Valid @RequestBody ResetPasswordReq req) {
        boolean success = userAccountService.resetPassword(req);
        return Result.success(success);
    }
}
