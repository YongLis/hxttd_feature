package com.ly.ttd.biz.feature.dem.sweb.service.account.impl;

import com.ly.ttd.biz.feature.dem.sweb.service.account.UserAccountAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.account.req.AccountAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.user.LoginUser;
import com.ly.ttd.biz.feature.dem.sweb.service.user.req.ResetPasswordReq;
import com.ly.ttd.feature.admin.api.dto.UserAccountDto;
import com.ly.ttd.feature.admin.api.dto.UserAccountUpdatePwd;
import com.ly.ttd.feature.admin.api.system.UserAccountService;
import com.ly.ttd.inf.rpc.api.annotation.Rpcwired;
import org.springframework.stereotype.Service;

/**
 * 用户账号服务实现
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Service
public class UserAccountAdminServiceImpl  implements UserAccountAdminService {

    @Rpcwired
    private UserAccountService userAccountService;

    @Override
    public void addAccount(AccountAddReq req) {
        UserAccountDto dto = new UserAccountDto();
        dto.setUserAccount(req.getUserAccount());
        dto.setPassword(req.getPassword());
        dto.setCrtUser(LoginUser.getLoginUserName());
        dto.setDeleted(false);
        userAccountService.add(dto);
    }
    @Override
    public void deleteAccount(Long id) {
        // 检查账户是否存在
        UserAccountDto dto = userAccountService.queryById(id);
        if (dto == null) {
            throw new RuntimeException("账户不存在");
        }

        // 逻辑删除
        dto.setDeleted(true);
        dto.setUptUser(LoginUser.getLoginUserName());
        userAccountService.update(dto);
    }


    @Override
    public UserAccountDto getUserAccountByUserAccount(String userName, String password) {
        return null;
    }

    @Override
    public void resetPassword(ResetPasswordReq req) {
        UserAccountUpdatePwd updatePwd = new UserAccountUpdatePwd();
        updatePwd.setUserAccount(req.getUserAccount());
        updatePwd.setOldPassword(req.getOldPassword());
        updatePwd.setNewPassword(req.getNewPassword());
        userAccountService.updatePassword(updatePwd);
    }
}
