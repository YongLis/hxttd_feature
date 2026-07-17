package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.FactorEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.req.FactorQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * factor Mapper
 *
 * @author yong.li
 * @since 2026-05-27
 */
@Mapper
public interface FactorMapper{

    /**
     * 分页查询连接器列表
     */
    List<FactorEntity> pageQuery(Page<FactorEntity> page, @Param("req") FactorQueryReq req);

    FactorEntity selectByResourceKey(String resourceKey);

    /**
     * 根据ID查询
     */
    FactorEntity selectById(@Param("id") Long id);

    
    /**
     * 根据 resource_key 查询总数
     */
    long selectCountByKey(@Param("key") String key);
}
