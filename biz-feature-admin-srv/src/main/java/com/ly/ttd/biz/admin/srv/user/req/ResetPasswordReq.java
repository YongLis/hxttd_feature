package com.ly.ttd.biz.admin.srv.user.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "账户ID不能为空")
    @Schema(description = "账户ID", example = "1")
    private Long id;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @Schema(description = "新密码", example = "new123456")
    private String newPassword;
}
