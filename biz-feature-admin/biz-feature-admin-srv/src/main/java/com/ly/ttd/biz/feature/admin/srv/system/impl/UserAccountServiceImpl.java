package com.ly.ttd.biz.feature.admin.srv.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.UserAccountEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.UserAccountMapper;
import com.ly.ttd.feature.admin.api.dto.UserAccountDto;
import com.ly.ttd.feature.admin.api.dto.UserAccountUpdatePwd;
import com.ly.ttd.feature.admin.api.system.UserAccountService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户账号 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class UserAccountServiceImpl implements UserAccountService {
    @Resource
    private UserAccountMapper userAccountMapper;

    @Override
    public Long add(UserAccountDto dto) throws BizException {
        UserAccountEntity entity = new UserAccountEntity();
        BeanUtils.copyProperties(dto, entity);
        userAccountMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(UserAccountDto dto) throws BizException {
        UserAccountEntity entity = new UserAccountEntity();
        BeanUtils.copyProperties(dto, entity);
        userAccountMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        userAccountMapper.deleteById(id);
    }

    @Override
    public UserAccountDto queryById(Long id) throws BizException {
        UserAccountEntity entity = userAccountMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        UserAccountDto dto = new UserAccountDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public List<UserAccountDto> queryAll() throws BizException {
        List<UserAccountEntity> list = userAccountMapper.selectAll();

        return list.stream().map(t -> {
            UserAccountDto dto = new UserAccountDto();
            BeanUtils.copyProperties(t, dto);
            return dto;
        }).toList();
    }

    @Override
    public void updatePassword(UserAccountUpdatePwd updatePwd) throws BizException {
        QueryWrapper<UserAccountEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", updatePwd.getUserAccount());
        queryWrapper.eq("password", updatePwd.getOldPassword());
        UserAccountEntity entity = userAccountMapper.selectOne(queryWrapper);
        if (entity == null) {
            throw new BizException("用户不存在或密码错误");
        }
        entity.setPassword(updatePwd.getNewPassword());
        userAccountMapper.updateById(entity);
    }
}
