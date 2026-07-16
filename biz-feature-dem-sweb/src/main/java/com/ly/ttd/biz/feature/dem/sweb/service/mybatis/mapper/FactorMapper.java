package com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.FactorQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.FactorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * factor Mapper
 *
 * @author yong.li
 * @since 2026-05-27
 */
@Mapper
public interface FactorMapper extends BaseMapper<FactorEntity> {

    /**
     * 分页查询连接器列表
     */
    IPage<FactorEntity> pageQuery(Page<FactorEntity> page, @Param("req") FactorQueryReq req);

    FactorEntity selectByResourceKey(String resourceKey);
}
