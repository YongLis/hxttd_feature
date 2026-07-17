package com.ly.ttd.biz.feature.dem.sweb.service.project.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper.ProjectMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.project.ProjectQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.project.req.ProjectQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.project.res.ProjectQueryRes;
import com.ly.ttd.feature.common.enums.FeatureResultCodeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

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

        // 返回 List 而不是 Page，避免 TooManyResultsException
        // PaginationInnerInterceptor 仍会处理 COUNT 和 LIMIT，但不会设置 page.records
        // 所以需要显式从返回值获取数据
        List<ProjectQueryRes> list = projectMapper.pageQuery(page, req);
        page.setRecords(list);

        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setData(page.getRecords());
        result.setCode(FeatureResultCodeEnum.SUCCESS.getCode());
        return result;
    }
}
