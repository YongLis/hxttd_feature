package com.ly.ttd.biz.feature.dem.sweb.service.account.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 账户更新请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class AccountUpdateReq {
    /**
     * 用户账户
     */
    @Schema(description = "用户账户", example = "admin")
    private String userAccount;
    @Schema(description = "旧密码")
    private String oldPassword;
    @Schema(description = "新密码")
    private String newPassword;
}
