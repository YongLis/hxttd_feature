package com.ly.ttd.biz.admin.srv.user.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author yong.li
 * @since 2026/4/13 10:47
 */

@Data
public class UserLoginRes {
    /**
     * 登录成功后sessionId
     */
    @Schema(description = "登录成功后sessionId")
    private String sessionId;

    /**
     * 用户信息
     */
    @Schema(description = "用户信息")
    private UserInfo userInfo;

}
