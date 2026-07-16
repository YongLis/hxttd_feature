package com.ly.ttd.biz.feature.dem.sweb.service.account;


import com.ly.ttd.biz.feature.dem.sweb.service.account.req.AccountAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.user.req.ResetPasswordReq;
import com.ly.ttd.feature.admin.api.dto.UserAccountDto;

/**
 * 用户账号服务
 *
 * @author yong.li
 * @since 2026-05-16
 */
public interface UserAccountAdminService {

    /**
     * 添加账户
     *
     * @param req 添加请求
     * @return 是否成功
     */
    void addAccount(AccountAddReq req);

    /**
     * 删除账户
     *
     * @param id 账户ID
     * @return 是否成功
     */
    void deleteAccount(Long id);

    /**
     * 重置密码
     *
     * @param req 重置密码请求
     * @return 是否成功
     */
    void resetPassword(ResetPasswordReq req);

    UserAccountDto getUserAccountByUserAccount(String userName, String password);
}
