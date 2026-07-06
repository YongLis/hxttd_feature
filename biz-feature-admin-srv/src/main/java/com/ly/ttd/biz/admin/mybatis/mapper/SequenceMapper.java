package com.ly.ttd.biz.admin.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.admin.mybatis.entity.SequenceEntity;
import com.ly.ttd.biz.admin.srv.sequence.req.SequenceQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 序列 Mapper
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Mapper
public interface SequenceMapper extends BaseMapper<SequenceEntity> {

    IPage<SequenceEntity> pageQuery(Page<SequenceEntity> page, @Param("req") SequenceQueryReq req);
}
