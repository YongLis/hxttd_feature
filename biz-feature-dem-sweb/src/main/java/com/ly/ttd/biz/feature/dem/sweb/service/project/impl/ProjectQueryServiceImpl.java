package com.ly.ttd.biz.feature.dem.sweb.service.project.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.ProjectMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.project.ProjectQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.project.req.ProjectQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.project.res.ProjectQueryRes;
import com.ly.ttd.feature.common.enums.FeatureResultCodeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 项目查询服务实现
 *
 * @author yong.li
 * @since 2026-06-23
 */
@Service
public class ProjectQueryServiceImpl implements ProjectQueryService {
    @Resource
    private ProjectMapper projectMapper;

    @Override
    public PageResult<ProjectQueryRes> pageQuery(ProjectQueryReq req) {
        PageResult<ProjectQueryRes> result = new PageResult<>();
        Page<ProjectQueryRes> page = new Page<>(req.getCurrent(), req.getPageSize());
        projectMapper.pageQuery(page, req);
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setData(page.getRecords());
        result.setCode(FeatureResultCodeEnum.SUCCESS.getCode());
        return result;
    }
}
