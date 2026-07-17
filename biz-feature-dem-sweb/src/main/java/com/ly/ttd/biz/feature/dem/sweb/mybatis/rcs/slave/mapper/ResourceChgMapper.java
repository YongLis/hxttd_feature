package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.ResourceChgEntity;

import java.util.List;

/**
 * 资源版本历史 Mapper
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Mapper
public interface ResourceChgMapper {

    /**
     * 根据ID查询
     */
    ResourceChgEntity selectById(@Param("id") Long id);
    /**
     * 查询全部
     */
    List<ResourceChgEntity> pageQuery(Page<ResourceChgEntity> page, @Param("resourceKey") String resourceKey);

}
