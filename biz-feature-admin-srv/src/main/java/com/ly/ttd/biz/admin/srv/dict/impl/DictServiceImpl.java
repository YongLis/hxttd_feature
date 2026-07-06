package com.ly.ttd.biz.admin.srv.dict.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ly.ttd.biz.admin.mybatis.entity.DictEntity;
import com.ly.ttd.biz.admin.mybatis.mapper.DictMapper;
import com.ly.ttd.biz.admin.srv.dict.DictService;
import com.ly.ttd.biz.admin.srv.dict.req.DictAddReq;
import com.ly.ttd.biz.admin.srv.dict.req.DictUpdateReq;
import com.ly.ttd.biz.admin.srv.user.LoginUser;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 字典管理服务实现
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, DictEntity> implements DictService {

    @Override
    public boolean addDict(DictAddReq req) {
        DictEntity entity = new DictEntity();
        entity.setSystemCode(req.getSystemCode());
        entity.setDictCode(req.getDictCode());
        entity.setDictName(req.getDictName());
        entity.setDeleted(false);
        return save(entity);
    }

    @Override
    public boolean updateDict(DictUpdateReq req) {
        DictEntity entity = getById(req.getId());
        if (entity == null) {
            throw new RuntimeException("字典不存在");
        }
        if (StringUtils.hasText(req.getSystemCode())) entity.setSystemCode(req.getSystemCode());
        if (StringUtils.hasText(req.getDictCode())) entity.setDictCode(req.getDictCode());
        if (StringUtils.hasText(req.getDictName())) entity.setDictName(req.getDictName());
        return updateById(entity);
    }

    @Override
    public boolean deleteDict(Long id) {
        DictEntity entity = getById(id);
        if (entity == null) {
            throw new RuntimeException("字典不存在");
        }
        UpdateWrapper<DictEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("deleted", true);
        updateWrapper.set("update_user", LoginUser.getLoginUserName());
        return update(updateWrapper);
    }

    @Override
    public DictEntity getDictCode(String systemCode, String dictCode) {
        QueryWrapper<DictEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("system_code", systemCode);
        queryWrapper.eq("dict_code", dictCode);
        return getOne(queryWrapper);
    }
}
