package com.ly.ttd.biz.admin.srv.user;

import com.ly.ttd.biz.admin.consts.LoginUserUtils;

/**
 * @author yong.li
 * @since 2026/6/3 09:31
 */
public class LoginUser {
    public static Long getProjectId() {
        return LoginUserUtils.INSTANCE.getProjectId();
    }

    public static String getLoginUserName() {
        if (LoginUserUtils.INSTANCE.getUserInfo() == null) {
            return "S";
        }
        return LoginUserUtils.INSTANCE.getUserInfo().getUserName();
    }
}
