package com.ly.ttd.biz.admin.srv.account;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ly.ttd.biz.admin.mybatis.entity.UserAccountEntity;
import com.ly.ttd.biz.admin.srv.account.req.AccountAddReq;
import com.ly.ttd.biz.admin.srv.account.req.AccountUpdateReq;
import com.ly.ttd.biz.admin.srv.user.req.ResetPasswordReq;

/**
 * 用户账号服务
 *
 * @author yong.li
 * @since 2026-05-16
 */
public interface UserAccountService extends IService<UserAccountEntity> {

    /**
     * 添加账户
     *
     * @param req 添加请求
     * @return 是否成功
     */
    boolean addAccount(AccountAddReq req);

    /**
     * 更新账户
     *
     * @param req 更新请求
     * @return 是否成功
     */
    boolean updateAccount(AccountUpdateReq req);

    /**
     * 删除账户
     *
     * @param id 账户ID
     * @return 是否成功
     */
    boolean deleteAccount(Long id);

    /**
     * 重置密码
     *
     * @param req 重置密码请求
     * @return 是否成功
     */
    boolean resetPassword(ResetPasswordReq req);
}
