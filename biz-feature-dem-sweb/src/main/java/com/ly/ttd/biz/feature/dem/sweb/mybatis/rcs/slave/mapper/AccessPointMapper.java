package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.AccessPointEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.access.req.AccessPointQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 接入点 Mapper
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Mapper
public interface AccessPointMapper{

    /**
     * 分页查询接入点
     */
    List<AccessPointEntity> pageQuery(Page<AccessPointEntity> page, @Param("req") AccessPointQueryReq req);


    /**
     * 根据项目ID查询接入点列表
     */
    List<AccessPointEntity> listByProjectId(Long projectId);

    /**
     * 根据编码查询接入点
     */
    AccessPointEntity getByCode(String code);

    /**
     * 根据 code 查询总数
     */
    long selectCountByKey(@Param("key") String key);

    AccessPointEntity selectById(Long id);

    /**
     * 查询全部
     */
    List<AccessPointEntity> selectAll();
}
