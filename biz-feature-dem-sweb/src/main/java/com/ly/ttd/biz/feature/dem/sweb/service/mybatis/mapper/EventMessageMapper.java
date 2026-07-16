package com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.service.eventMessage.req.FeatureTraceQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.EventMessageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 事件消息 Mapper
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Mapper
public interface EventMessageMapper extends BaseMapper<EventMessageEntity> {

    IPage<EventMessageEntity> pageQuery(Page<EventMessageEntity> page, @Param("req") FeatureTraceQueryReq req);
}
