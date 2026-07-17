package com.ly.ttd.biz.feature.dem.sweb.service.project.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.ProjectUserEntity;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper.ProjectUserMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.project.ProjectUserQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.project.req.ProjectUserQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.project.res.ProjectUserQueryRes;
import com.ly.ttd.feature.common.enums.FeatureResultCodeEnum;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yong.li
 * @since 2026/7/3 11:59
 */
@Service
public class ProjectUserQueryServiceImpl implements ProjectUserQueryService {
    @Resource
    private ProjectUserMapper projectUserMapper;

    @Override
    public PageResult<ProjectUserQueryRes> pageQuery(ProjectUserQueryReq req) {
        PageResult<ProjectUserQueryRes> result = new PageResult<>();
        Page<ProjectUserEntity> page = new Page<>(req.getCurrent(), req.getPageSize());
        List<ProjectUserEntity> records = projectUserMapper.pageQuery(page, req);
        page.setRecords(records);
        List<ProjectUserQueryRes> list = records.stream()
                .map(t -> entityConvertRes(t)).toList();

        result.setData(list);
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setCode(FeatureResultCodeEnum.SUCCESS.getCode());
        return result;
    }

    @Override
    public List<ProjectUserQueryRes> getAll(Long projectId) {
        List<ProjectUserEntity> list = projectUserMapper.getAll(projectId);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream()
                    .map(t -> entityConvertRes(t)).toList();
        }
        return new ArrayList<>();
    }

    private static ProjectUserQueryRes entityConvertRes(ProjectUserEntity t) {
        ProjectUserQueryRes res = new ProjectUserQueryRes();
        res.setId(t.getId());
        res.setProjectId(t.getProjectId());
        res.setUserAccount(t.getUserAccount());
        res.setCrtUser(t.getCrtUser());
        res.setUptUser(t.getUptUser());
        res.setCrtTime(t.getCrtTime());
        res.setUptTime(t.getUptTime());
        res.setDeleted(t.getDeleted());
        return res;
    }
}
