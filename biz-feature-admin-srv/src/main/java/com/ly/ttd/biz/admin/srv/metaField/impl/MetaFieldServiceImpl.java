package com.ly.ttd.biz.admin.srv.metaField.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ly.ttd.biz.admin.mybatis.entity.MetaFieldEntity;
import com.ly.ttd.biz.admin.mybatis.mapper.MetaFieldMapper;
import com.ly.ttd.biz.admin.srv.metaField.MetaFieldService;
import com.ly.ttd.biz.admin.srv.metaField.req.MetaFieldAddReq;
import com.ly.ttd.biz.admin.srv.metaField.req.MetaFieldUpdateReq;
import com.ly.ttd.biz.admin.srv.project.ProjectService;
import com.ly.ttd.consts.enums.ResourceTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 元字段服务实现
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Service
public class MetaFieldServiceImpl extends ServiceImpl<MetaFieldMapper, MetaFieldEntity> implements MetaFieldService {

    @Resource
    private ProjectService projectService;

    @Override
    public boolean addMetaField(MetaFieldAddReq req) {
        String key = projectService.getResourceKey(req.getProjectId(), ResourceTypeEnum.META.getPrefix(), req.getResourceKey());
        req.setResourceKey(key);
        // 检查资源键唯一性
        LambdaQueryWrapper<MetaFieldEntity> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(MetaFieldEntity::getResourceKey, req.getResourceKey());
        checkWrapper.eq(MetaFieldEntity::getDeleted, false);
        if (count(checkWrapper) > 0) {
            throw new RuntimeException("资源键已存在");
        }

        MetaFieldEntity entity = new MetaFieldEntity();
        entity.setResourceKey(req.getResourceKey());
        entity.setResourceName(req.getResourceName());
        entity.setVersion("V" + System.currentTimeMillis());
        entity.setLanguage(req.getLanguage());
        entity.setScript(req.getScript());
        entity.setReturnType(req.getReturnType());
        entity.setDefaultValue(req.getDefaultValue());
        entity.setExceptionValue(req.getExceptionValue());
        entity.setCategoryTag(req.getCategoryTag() != null ? req.getCategoryTag() : "public");

        // 从当前上下文获取租户和项目ID
        entity.setProjectId(req.getProjectId());
        entity.setDeleted(false);


        return save(entity);
    }

    @Override
    public boolean updateMetaField(MetaFieldUpdateReq req) {
        MetaFieldEntity entity = getById(req.getId());
        if (entity == null) {
            throw new RuntimeException("元字段不存在");
        }

        if (StringUtils.hasText(req.getResourceName())) {
            entity.setResourceName(req.getResourceName());
        }
        if (StringUtils.hasText(req.getLanguage())) {
            entity.setLanguage(req.getLanguage());
        }
        if (StringUtils.hasText(req.getScript())) {
            entity.setScript(req.getScript());
        }
        if (StringUtils.hasText(req.getReturnType())) {
            entity.setReturnType(req.getReturnType());
        }
        entity.setVersion("V" + System.currentTimeMillis());
        entity.setDefaultValue(req.getDefaultValue());
        entity.setExceptionValue(req.getExceptionValue());
        if (req.getCategoryTag() != null) {
            entity.setCategoryTag(req.getCategoryTag());
        }

        return updateById(entity);
    }

    @Override
    public boolean deleteMetaField(Long id) {
        MetaFieldEntity entity = getById(id);
        if (entity == null) {
            throw new RuntimeException("元字段不存在");
        }
        entity.setDeleted(true);
        return updateById(entity);
    }
}
