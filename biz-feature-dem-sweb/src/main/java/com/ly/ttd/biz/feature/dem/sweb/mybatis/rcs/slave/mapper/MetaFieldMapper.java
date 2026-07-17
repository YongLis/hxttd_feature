package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.MetaFieldEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.req.MetaFieldQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 元字段 Mapper
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Mapper
public interface MetaFieldMapper {

    /**
     * 分页查询元字段
     *
     * @param page 分页参数
     * @return 分页结果
     */
    List<MetaFieldEntity> pageQuery(Page<MetaFieldEntity> page, MetaFieldQueryReq req);


    List<MetaFieldEntity> getByProjectId(Long projectId);

    MetaFieldEntity selectByResourceKey(String resourceKey);

    /**
     * 根据ID查询
     */
    MetaFieldEntity selectById(@Param("id") Long id);
    
    /**
     * 根据 resource_key 查询总数
     */
    long selectCountByKey(@Param("key") String key);
}
