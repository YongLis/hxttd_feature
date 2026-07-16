package com.ly.ttd.biz.feature.dem.sweb.service.user.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 重置密码请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class ResetPasswordReq {

    /**
     * 账户ID
     */
    @NotBlank(message = "账户账号不能为空")
    @Schema(description = "账户账号", example = "admin")
    private String userAccount;

    @NotBlank(message = "旧密码不能为空")
    @Schema(description = "旧密码", example = "admin")
    private String oldPassword;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @Schema(description = "新密码", example = "new123456")
    private String newPassword;
}
