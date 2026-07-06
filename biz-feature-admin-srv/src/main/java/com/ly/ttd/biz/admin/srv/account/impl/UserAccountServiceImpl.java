package com.ly.ttd.biz.admin.srv.account.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ly.ttd.biz.admin.mybatis.entity.UserAccountEntity;
import com.ly.ttd.biz.admin.mybatis.mapper.UserAccountMapper;
import com.ly.ttd.biz.admin.srv.account.UserAccountService;
import com.ly.ttd.biz.admin.srv.account.req.AccountAddReq;
import com.ly.ttd.biz.admin.srv.account.req.AccountUpdateReq;
import com.ly.ttd.biz.admin.srv.user.req.ResetPasswordReq;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户账号服务实现
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccountEntity> implements UserAccountService {

    @Resource
    private UserAccountMapper userAccountMapper;

    @Override
    public boolean addAccount(AccountAddReq req) {
        // 检查账户是否已存在
        LambdaQueryWrapper<UserAccountEntity> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(UserAccountEntity::getUserAccount, req.getUserAccount());
        if (count(checkWrapper) > 0) {
            throw new RuntimeException("账户已存在");
        }

        // 创建账户
        UserAccountEntity entity = new UserAccountEntity();
        entity.setUserAccount(req.getUserAccount());
        entity.setPassword(req.getPassword()); // 注意：实际应用中应该对密码进行加密
        entity.setDeleted(false);

        return save(entity);
    }

    @Override
    public boolean updateAccount(AccountUpdateReq req) {
        // 检查账户是否存在
        UserAccountEntity entity = getById(req.getId());
        if (entity == null) {
            throw new RuntimeException("账户不存在");
        }

        // 如果修改了账户名称，检查是否重复
        if (StringUtils.hasText(req.getUserAccount()) && !req.getUserAccount().equals(entity.getUserAccount())) {
            LambdaQueryWrapper<UserAccountEntity> checkWrapper = new LambdaQueryWrapper<>();
            checkWrapper.eq(UserAccountEntity::getUserAccount, req.getUserAccount());
            if (count(checkWrapper) > 0) {
                throw new RuntimeException("账户名称已存在");
            }
            entity.setUserAccount(req.getUserAccount());
        }

        return updateById(entity);
    }

    @Override
    public boolean deleteAccount(Long id) {
        // 检查账户是否存在
        UserAccountEntity entity = getById(id);
        if (entity == null) {
            throw new RuntimeException("账户不存在");
        }

        // 逻辑删除
        entity.setDeleted(true);
        return updateById(entity);
    }

    @Override
    public boolean resetPassword(ResetPasswordReq req) {
        // 检查账户是否存在
        UserAccountEntity entity = getById(req.getId());
        if (entity == null) {
            throw new RuntimeException("账户不存在");
        }

        // 更新密码（注意：实际应用中应该对密码进行加密）
        entity.setPassword(req.getNewPassword());
        return updateById(entity);
    }
}
