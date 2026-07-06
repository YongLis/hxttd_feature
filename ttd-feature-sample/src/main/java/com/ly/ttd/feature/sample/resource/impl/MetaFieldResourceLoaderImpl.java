package com.ly.ttd.feature.sample.resource.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ly.ttd.feature.cfg.resource.MetaFieldResourceLoader;
import com.ly.ttd.feature.common.model.meta.MetaFieldModel;
import com.ly.ttd.feature.sample.mybatis.entity.MetaFieldEntity;
import com.ly.ttd.feature.sample.mybatis.mapper.MetaFieldMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 元字段资源加载实现
 * <p>
 * 从 MySQL（ttd_meta_field 表）读取元字段配置，转换为引擎用的 {@link MetaFieldModel}。
 *
 * @author yong.li
 * @since 2026/6/29
 */
@Slf4j
@Service
public class MetaFieldResourceLoaderImpl implements MetaFieldResourceLoader {

    @Resource
    private MetaFieldMapper metaFieldMapper;

    @Override
    public List<MetaFieldModel> loadResource(Long projectId) {
        List<MetaFieldEntity> entities = metaFieldMapper.selectList(
                new LambdaQueryWrapper<MetaFieldEntity>()
                        .eq(MetaFieldEntity::getProjectId, projectId));
        if (CollectionUtils.isEmpty(entities)) {
            log.info("未加载到元字段资源, projectId={}", projectId);
            return Collections.emptyList();
        }
        List<MetaFieldModel> models = entities.stream().map(this::convert).collect(Collectors.toList());
        log.info("加载元字段资源完成, projectId={}, count={}", projectId, models.size());
        return models;
    }

    private MetaFieldModel convert(MetaFieldEntity entity) {
        MetaFieldModel model = new MetaFieldModel();
        model.setResourceKey(entity.getResourceKey());
        model.setResourceName(entity.getResourceName());
        model.setVersion(entity.getVersion());
        model.setProjectId(entity.getProjectId());
        model.setReturnType(entity.getReturnType());
        model.setDefaultValue(entity.getDefaultValue());
        model.setExceptionValue(entity.getExceptionValue());
        model.setLanguage(entity.getLanguage());
        model.setScript(entity.getScript());
        model.setPointCode(entity.getCategoryTag());
        return model;
    }
}
