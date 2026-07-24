package com.ly.ttd.biz.feature.dem.sweb.service.account.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.account.UserAccountAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.account.req.AccountAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.user.LoginUser;
import com.ly.ttd.biz.feature.dem.sweb.service.user.req.ResetPasswordReq;
import com.ly.ttd.feature.admin.api.dto.UserAccountDto;
import com.ly.ttd.feature.admin.api.dto.UserAccountUpdatePwd;
import com.ly.ttd.feature.admin.api.system.UserAccountService;
import com.ly.ttd.inf.rpc.api.annotation.Rpcwired;
import com.ly.ttd.utils.Md5Util;
import org.apache.commons.lang3.StringUtils;
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
        if(StringUtils.isEmpty(req.getUserAccount()) || StringUtils.isEmpty(req.getPassword())){
            throw new BizException("用户名或密码不允许为空");
        }
        UserAccountDto dto = new UserAccountDto();
        dto.setUserAccount(req.getUserAccount());
        dto.setPassword(Md5Util.MD5(req.getPassword()));
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
        if(userName == null || password == null){
            throw new RuntimeException("用户名或密码不能为空");
        }
        String pwd = Md5Util.MD5(password);
        return userAccountService.getUserAccountByUserAccount(userName, pwd);
    }

    @Override
    public void resetPassword(ResetPasswordReq req) {
        if(StringUtils.isEmpty(req.getUserAccount()) || StringUtils.isEmpty(req.getOldPassword()) || StringUtils.isEmpty(req.getNewPassword())){
            throw new BizException("用户名、旧密码、新密码不能为空");
        }
        UserAccountUpdatePwd updatePwd = new UserAccountUpdatePwd();
        updatePwd.setUserAccount(req.getUserAccount());
        updatePwd.setOldPassword(Md5Util.MD5(req.getOldPassword()));
        updatePwd.setNewPassword(Md5Util.MD5(req.getNewPassword()));
        userAccountService.updatePassword(updatePwd);
    }
}
