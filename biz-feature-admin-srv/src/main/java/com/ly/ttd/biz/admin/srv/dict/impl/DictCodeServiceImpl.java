package com.ly.ttd.biz.admin.srv.dict.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ly.ttd.biz.admin.consts.LoginUserUtils;
import com.ly.ttd.biz.admin.mybatis.entity.DictCodeEntity;
import com.ly.ttd.biz.admin.mybatis.mapper.DictCodeMapper;
import com.ly.ttd.biz.admin.srv.dict.DictCodeService;
import com.ly.ttd.biz.admin.srv.dict.req.DictCodeAddReq;
import com.ly.ttd.biz.admin.srv.dict.req.DictCodeUpdateReq;
import com.ly.ttd.biz.admin.srv.user.LoginUser;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 字典键值管理服务实现
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Service
public class DictCodeServiceImpl extends ServiceImpl<DictCodeMapper, DictCodeEntity> implements DictCodeService {

    @Override
    public boolean addDictCode(DictCodeAddReq req) {
        DictCodeEntity entity = new DictCodeEntity();
        entity.setDictId(req.getDictId());
        entity.setDictKey(req.getDictKey());
        entity.setDictValue(req.getDictValue());
        entity.setDeleted(false);
        return save(entity);
    }

    @Override
    public boolean updateDictCode(DictCodeUpdateReq req) {
        DictCodeEntity entity = getById(req.getId());
        if (entity == null) {
            throw new RuntimeException("字典键值不存在");
        }
        if (req.getDictId() != null) entity.setDictId(req.getDictId());
        if (StringUtils.hasText(req.getDictKey())) entity.setDictKey(req.getDictKey());
        if (StringUtils.hasText(req.getDictValue())) entity.setDictValue(req.getDictValue());
        return updateById(entity);
    }

    @Override
    public boolean deleteDictCode(Long id) {
        DictCodeEntity entity = getById(id);
        if (entity == null) {
            throw new RuntimeException("字典键值不存在");
        }
        UpdateWrapper<DictCodeEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("upt_user", LoginUser.getLoginUserName());
        updateWrapper.set("deleted", true);
        return update(updateWrapper);
    }

    @Override
    public List<DictCodeEntity> getByDictId(Long dictId) {
        QueryWrapper<DictCodeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_id", dictId);
        return list(wrapper);
    }

    @Override
    public void addDictCode(Long dictId, String dictKey, String dictValue) {
        DictCodeEntity codeEntity = new DictCodeEntity();
        codeEntity.setDictId(dictId);
        codeEntity.setDictKey(dictKey);
        codeEntity.setDictValue(dictValue);
        codeEntity.setDeleted(false);
        codeEntity.setCrtUser(LoginUserUtils.INSTANCE.getUserInfo().getUserName());
        save(codeEntity);
    }

}
