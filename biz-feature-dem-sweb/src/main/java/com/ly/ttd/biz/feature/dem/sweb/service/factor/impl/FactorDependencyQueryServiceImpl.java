package com.ly.ttd.biz.feature.dem.sweb.service.factor.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.FactorDependencyQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.FactorDependencyQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.res.FactorDependencyQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.FactorDependencyEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.FactorDependencyMapper;
import com.ly.ttd.feature.common.enums.FeatureResultCodeEnum;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 指标血缘查询服务实现
 *
 * @author yong.li
 * @since 2026-06-30
 */
@Service
public class FactorDependencyQueryServiceImpl implements FactorDependencyQueryService {

    @Resource
    private FactorDependencyMapper factorDependencyMapper;

    @Override
    public PageResult<FactorDependencyQueryRes> pageQuery(FactorDependencyQueryReq req) {
        PageResult<FactorDependencyQueryRes> result = new PageResult<>();
        Page<FactorDependencyEntity> page = new Page<>(req.getCurrent(), req.getPageSize());
        factorDependencyMapper.pageQuery(page, req);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            result.setData(page.getRecords().stream().map(t -> {
                FactorDependencyQueryRes res = new FactorDependencyQueryRes();
                res.setId(t.getId());
                res.setProjectId(t.getProjectId());
                res.setParent(t.getParent());
                res.setChild(t.getChild());
                return res;
            }).collect(Collectors.toList()));
        }
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setCode(FeatureResultCodeEnum.SUCCESS.getCode());
        return result;
    }

    @Override
    public List<FactorDependencyEntity> queryDependency(Long projectId, String parent) {
        List<FactorDependencyEntity> result = factorDependencyMapper.queryDependency(projectId, Arrays.asList(parent));
        if (CollectionUtils.isNotEmpty(result)) {
            List<String> childs = result.stream().map(FactorDependencyEntity::getChild)
                    .distinct().collect(Collectors.toList());
            queryChild(projectId, result, childs);
        }
        return result;
    }

    @Override
    public List<FactorDependencyEntity> queryUpstreamDependency(Long projectId, String child) {
        List<FactorDependencyEntity> result = factorDependencyMapper.queryUpstreamDependency(projectId, Arrays.asList(child));
        if (CollectionUtils.isNotEmpty(result)) {
            List<String> parents = result.stream().map(FactorDependencyEntity::getParent)
                    .distinct().collect(Collectors.toList());
            queryParent(projectId, result, parents);
        }
        return result;
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
}
