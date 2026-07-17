package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.EventMessageEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.eventMessage.req.FeatureTraceQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 事件消息 Mapper
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Mapper
public interface EventMessageMapper{

    List<EventMessageEntity> pageQuery(Page<EventMessageEntity> page, @Param("req") FeatureTraceQueryReq req);

    /**
     * 根据ID查询
     */
    EventMessageEntity selectById(@Param("id") Long id);
}
