package com.ly.ttd.biz.feature.dem.sweb.service.pipe.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.PipeTask;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper.PipeTaskMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.pipe.PipeTaskQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.pipe.req.PipeTaskQueryReq;
import com.ly.ttd.feature.admin.api.dto.PipeTaskDto;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yong.li
 * @since 2026/7/24 12:02
 */
@Service
public class PipeTaskQueryServiceImpl implements PipeTaskQueryService {
    @Resource
    private PipeTaskMapper pipeTaskMapper;
    @Override
    public PageResult<PipeTaskDto> pageQuery(PipeTaskQueryReq req) {
        PageResult<PipeTaskDto> result = new PageResult<>();
        Page<PipeTask> page = new Page<>(req.getCurrent(), req.getPageSize());
        List<PipeTask> records = pipeTaskMapper.pageQuery(page, req);
        page.setRecords(records);
        if(records != null && !records.isEmpty()){
            List<PipeTaskDto> dtos = records.stream().map(t -> {
                PipeTaskDto dto = new PipeTaskDto();
                BeanUtils.copyProperties(t, dto);
                return dto;
            }).toList();
            result.setData(dtos);
        }

        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setCode("0000");
        return result;
    }
}
