package com.ly.ttd.biz.feature.dem.sweb.service.user.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户信息
 *
 * @author yong.li
 * @since 2026/4/30 09:58
 */
@Data
public class UserInfo {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "角色")
    private String role;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String phone;

}
