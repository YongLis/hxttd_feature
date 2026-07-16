package com.ly.ttd.feature.admin.api.dto;

import lombok.Data;

/**
 * @author yong.li
 * @since 2026/7/14 14:44
 */
@Data
public class UserAccountUpdatePwd {

    private String userAccount;
    private String oldPassword;
    private String newPassword;
}
