package com.ly.ttd.biz.feature.dem.sweb.service.account.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 账户添加请求
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class AccountAddReq {

    /**
     * 用户账户
     */
    @NotBlank(message = "账户名称不能为空")
    @Size(max = 256, message = "账户名称不能超过256个字符")
    @Schema(description = "用户账户", example = "admin")
    private String userAccount;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度不能少于6个字符")
    @Schema(description = "密码(至少6位)", example = "123456")
    private String password;
}
