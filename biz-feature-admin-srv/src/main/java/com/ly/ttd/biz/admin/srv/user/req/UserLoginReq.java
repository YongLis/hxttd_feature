package com.ly.ttd.biz.admin.srv.user.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author yong.li
 * @since 2026/4/13 10:46
 */
@Data
public class UserLoginReq {

    @Schema(description = "用户名", example = "admin")
    private String userName;

    @Schema(description = "密码", example = "123456")
    private String password;

}
