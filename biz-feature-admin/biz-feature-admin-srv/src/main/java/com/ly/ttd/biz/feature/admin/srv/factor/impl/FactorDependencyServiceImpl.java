package com.ly.ttd.biz.feature.admin.srv.factor.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.FactorDependencyEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.FactorDependencyMapper;
import com.ly.ttd.feature.admin.api.dto.FactorDependencyDto;
import com.ly.ttd.feature.admin.api.factor.FactorDependencyService;
import jakarta.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 指标血缘 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class FactorDependencyServiceImpl implements FactorDependencyService {
    @Resource
    private FactorDependencyMapper factorDependencyMapper;

    @Override
    public Long add(FactorDependencyDto dto) throws BizException {
        FactorDependencyEntity entity = new FactorDependencyEntity();
        BeanUtils.copyProperties(dto, entity);
        factorDependencyMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(FactorDependencyDto dto) throws BizException {
        FactorDependencyEntity entity = new FactorDependencyEntity();
        BeanUtils.copyProperties(dto, entity);
        factorDependencyMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        factorDependencyMapper.deleteById(id);
    }

    @Override
    public FactorDependencyDto queryById(Long id) throws BizException {
        FactorDependencyEntity entity = factorDependencyMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        FactorDependencyDto dto = new FactorDependencyDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public List<FactorDependencyDto> queryDependency(Long projectId, String parent) {
        List<FactorDependencyEntity> result = factorDependencyMapper.queryDependency(projectId, Arrays.asList(parent));
        if (CollectionUtils.isNotEmpty(result)) {
            List<String> childs = result.stream().map(FactorDependencyEntity::getChild)
                    .distinct().collect(Collectors.toList());
            queryChild(projectId, result, childs);
        }

        return entityConvertDto(result);
    }

    private static List<FactorDependencyDto> entityConvertDto(List<FactorDependencyEntity> result) {
        List<FactorDependencyDto> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(result)) {
            list.addAll(result.stream().map(t -> {
                FactorDependencyDto dto = new FactorDependencyDto();
                BeanUtils.copyProperties(t, dto);
                return dto;
            }).toList());
        }
        return list;
    }

    private void queryChild(Long projectId, List<FactorDependencyEntity> result, List<String> parents) {
        if (CollectionUtils.isEmpty(parents)) {
            return;
        }

        List<FactorDependencyEntity> tmp = factorDependencyMapper.queryDependency(projectId, parents);
        if (CollectionUtils.isNotEmpty(tmp)) {
            result.addAll(tmp);

            List<String> childs = tmp.stream().map(FactorDependencyEntity::getChild)
                    .distinct().collect(Collectors.toList());
            queryChild(projectId, result, childs);
        }
    }

    private void queryParent(Long projectId, List<FactorDependencyEntity> result, List<String> childs) {
        if (CollectionUtils.isEmpty(childs)) {
            return;
        }

        List<FactorDependencyEntity> tmp = factorDependencyMapper.queryUpstreamDependency(projectId, childs);
        if (CollectionUtils.isNotEmpty(tmp)) {
            result.addAll(tmp);

            List<String> parents = tmp.stream().map(FactorDependencyEntity::getParent)
                    .distinct().collect(Collectors.toList());
            queryParent(projectId, result, parents);
        }
    }

    @Override
    public void removeDependency(Long projectId, String parent) {
        factorDependencyMapper.removeDependency(projectId, parent);
    }

    @Override
    public void addDependency(Long projectId, String parent, String parentType, List<String> childs, String user) {
        List<FactorDependencyEntity> dependencyEntities = new ArrayList<>();
        childs.forEach(child -> {
            FactorDependencyEntity entity = new FactorDependencyEntity();
            entity.setProjectId(projectId);
            entity.setParent(parent);
            entity.setParentType(parentType);
            entity.setChild(child);
            entity.setCrtUser(user);
            entity.setDeleted(false);
            dependencyEntities.add(entity);
        });

        if (CollectionUtils.isEmpty(dependencyEntities)) {
            return;
        }
        factorDependencyMapper.insert(dependencyEntities);
    }

    @Override
    public List<FactorDependencyDto> queryUpstreamDependency(Long projectId, String child) {
        List<FactorDependencyEntity> result = factorDependencyMapper.queryUpstreamDependency(projectId, Arrays.asList(child));
        if (CollectionUtils.isNotEmpty(result)) {
            List<String> parents = result.stream().map(FactorDependencyEntity::getParent)
                    .distinct().collect(Collectors.toList());
            queryParent(projectId, result, parents);
        }
        return entityConvertDto(result);
    }
}
