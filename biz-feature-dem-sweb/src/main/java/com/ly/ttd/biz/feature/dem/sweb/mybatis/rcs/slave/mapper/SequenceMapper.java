package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.SequenceEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.sequence.req.SequenceQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 序列 Mapper
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Mapper
public interface SequenceMapper {
    List<SequenceEntity> pageQuery(Page<SequenceEntity> page, @Param("req") SequenceQueryReq req);

    /**
     * 根据ID查询
     */
    SequenceEntity selectById(@Param("id") Long id);

}
