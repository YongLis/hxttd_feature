package com.ly.ttd.biz.admin.srv.account.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
     * 账户ID
     */
    @NotNull(message = "账户ID不能为空")
    @Schema(description = "账户ID", example = "1")
    private Long id;

    /**
     * 用户账户
     */
    @Schema(description = "用户账户", example = "admin")
    private String userAccount;
}
