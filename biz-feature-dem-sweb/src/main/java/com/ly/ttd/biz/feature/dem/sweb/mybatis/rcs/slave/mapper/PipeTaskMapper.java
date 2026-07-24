package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.PipeTask;
import com.ly.ttd.biz.feature.dem.sweb.service.pipe.req.PipeTaskQueryReq;

import java.util.List;

/**
 * 数据管道任务 Mapper
 *
 * @author yong.li
 * @since 2026/7/14
 */
public interface PipeTaskMapper {
    List<PipeTask> pageQuery(Page<PipeTask> page, PipeTaskQueryReq req);

}
