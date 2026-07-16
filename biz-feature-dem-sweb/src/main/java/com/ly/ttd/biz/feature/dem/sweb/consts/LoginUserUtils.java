package com.ly.ttd.biz.feature.dem.sweb.consts;

import com.ly.ttd.biz.feature.dem.sweb.service.user.res.UserInfo;

/**
 * @author yong.li
 * @since 2026/4/30 23:34
 */
public enum LoginUserUtils {
    INSTANCE;

    private UserInfo userInfo = new UserInfo();
    private Long projectId;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
