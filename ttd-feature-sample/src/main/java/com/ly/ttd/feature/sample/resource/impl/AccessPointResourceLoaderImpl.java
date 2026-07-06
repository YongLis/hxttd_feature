package com.ly.ttd.feature.sample.resource.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ly.ttd.feature.cfg.resource.AccessPointResourceLoader;
import com.ly.ttd.feature.common.model.AccessPointModel;
import com.ly.ttd.feature.sample.mybatis.entity.AccessPointEntity;
import com.ly.ttd.feature.sample.mybatis.mapper.AccessPointMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接入点资源加载实现
 * <p>
 * 从 MySQL（ttd_access_point 表）读取接入点配置，转换为引擎用的 {@link AccessPointModel}。
 *
 * @author yong.li
 * @since 2026/6/29
 */
@Slf4j
@Service
public class AccessPointResourceLoaderImpl implements AccessPointResourceLoader {

    @Resource
    private AccessPointMapper accessPointMapper;

    @Override
    public List<AccessPointModel> loadResource(Long projectId) {
        List<AccessPointEntity> entities = accessPointMapper.selectList(
                new LambdaQueryWrapper<AccessPointEntity>()
                        .eq(AccessPointEntity::getProjectId, projectId));
        if (CollectionUtils.isEmpty(entities)) {
            log.info("未加载到接入点资源, projectId={}", projectId);
            return Collections.emptyList();
        }
        List<AccessPointModel> models = entities.stream().map(this::convert).collect(Collectors.toList());
        log.info("加载接入点资源完成, projectId={}, count={}", projectId, models.size());
        return models;
    }

    private AccessPointModel convert(AccessPointEntity entity) {
        AccessPointModel model = new AccessPointModel();
        model.setCode(entity.getCode());
        model.setName(entity.getName());
        model.setDescription(entity.getRemark());
        model.setProjectId(entity.getProjectId());
        model.setVersion(entity.getVersion());
        model.setStatus("ENABLED");
        model.setOperationType("QUERY");
        model.setSubModels(Collections.emptyList());
        return model;
    }
}
