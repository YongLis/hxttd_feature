package com.ly.ttd.biz.admin.srv.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ly.ttd.biz.admin.cache.UserCache;
import com.ly.ttd.biz.admin.consts.LoginUserUtils;
import com.ly.ttd.biz.admin.mybatis.entity.ProjectUserEntity;
import com.ly.ttd.biz.admin.mybatis.entity.UserAccountEntity;
import com.ly.ttd.biz.admin.mybatis.mapper.ProjectUserMapper;
import com.ly.ttd.biz.admin.req.BaseRequest;
import com.ly.ttd.biz.admin.srv.account.UserAccountService;
import com.ly.ttd.biz.admin.srv.user.UserService;
import com.ly.ttd.biz.admin.srv.user.req.UserLoginReq;
import com.ly.ttd.biz.admin.srv.user.res.UserCurrentRes;
import com.ly.ttd.biz.admin.srv.user.res.UserInfo;
import com.ly.ttd.biz.admin.srv.user.res.UserLoginRes;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author yong.li
 * @since 2026/4/13 10:49
 */
@Service
public class UserServiceImpl implements UserService {
    public static String USER_LOGIN_ERROR_COUNT = "USER_LOGIN_ERROR_COUNT";

    @Resource
    private UserAccountService userAccountService;

    @Resource
    private ProjectUserMapper projectUserMapper;

    @Override
    public UserLoginRes login(UserLoginReq req) throws FeatureBizException {
        UserLoginRes res = new UserLoginRes();
        if (StringUtils.isEmpty(req.getUserName()) || StringUtils.isEmpty(req.getPassword())) {
            throw new FeatureBizException("0001", "用户名或密码不能为空");
        }

        Object errorCount = UserCache.INSTANCE.getIfPresent(USER_LOGIN_ERROR_COUNT);
        if (null != errorCount && ((Integer) errorCount) >= 5) {
            throw new FeatureBizException("0002", "登录失败次数超过5次，账号已被锁定");
        }

        if (isLoginSuccess(req.getUserName(), req.getPassword())) {
            String sessionId = UUID.randomUUID().toString();
            res.setSessionId(sessionId);
            // 创建用户信息
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName(req.getUserName());
            // 所有登录用户都是 admin 角色
            userInfo.setRole("admin");

            // 缓存用户信息到 Caffeine，30分钟过期
            UserCache.INSTANCE.put(sessionId, userInfo);

            res.setUserInfo(userInfo);
        } else {
            throw new FeatureBizException("0003", "账号或密码错误");
        }
        return res;
    }

    @Override
    public Boolean logout(BaseRequest req) {
        // 退出登录时清除缓存
        if (StringUtils.isNoneEmpty(req.getSessionId())) {
            UserCache.INSTANCE.delete(req.getSessionId());
        }
        return true;
    }

    @Override
    public UserLoginRes getCurrentUser() throws FeatureBizException {
        UserLoginRes res = new UserLoginRes();
        UserInfo userInfo = LoginUserUtils.INSTANCE.getUserInfo();
        if (null == userInfo) {
            throw new FeatureBizException("0004", "用户未登录");
        } else {
            res.setUserInfo(userInfo);
        }
        return res;
    }

    @Override
    public UserCurrentRes getCurrentUserInfo() throws FeatureBizException {
        UserInfo userInfo = LoginUserUtils.INSTANCE.getUserInfo();
        if (null == userInfo) {
            throw new FeatureBizException("0004", "用户未登录");
        }

        String userName = userInfo.getUserName();
        if (StringUtils.isEmpty(userName)) {
            throw new FeatureBizException("0005", "用户名称为空");
        }

        // 查询用户的项目关联信息
        LambdaQueryWrapper<ProjectUserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectUserEntity::getUserAccount, userName);
        wrapper.eq(ProjectUserEntity::getDeleted, false);

        List<ProjectUserEntity> projectUsers = projectUserMapper.selectList(wrapper);

        UserCurrentRes res = new UserCurrentRes();
        res.setUserAccount(userName);
        res.setRole(userInfo.getRole());

        if (projectUsers != null && !projectUsers.isEmpty()) {
            // 收集所有项目ID（去重）
            List<Long> projectIds = projectUsers.stream()
                    .map(ProjectUserEntity::getProjectId)
                    .distinct()
                    .collect(Collectors.toList());
            res.setProjectIds(projectIds);

            // 默认选中第一个项目ID
            // res.setSelectedProjectId(projectIds.get(0));
        }

        return res;
    }

    /**
     * 验证登录是否成功
     *
     * @param userName 用户名
     * @param password 密码
     * @return 是否成功
     */
    private boolean isLoginSuccess(String userName, String password) {
        // 从数据库查询用户
        LambdaQueryWrapper<UserAccountEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAccountEntity::getUserAccount, userName);
        wrapper.eq(UserAccountEntity::getDeleted, false);

        UserAccountEntity user = userAccountService.getOne(wrapper);

        // 用户不存在或密码不匹配
        if (user == null) {
            return false;
        }

        // 注意：实际生产环境应该使用 BCrypt 或其他加密算法进行密码比对
        // 这里直接比较明文密码
        return password.equals(user.getPassword());
    }

}
