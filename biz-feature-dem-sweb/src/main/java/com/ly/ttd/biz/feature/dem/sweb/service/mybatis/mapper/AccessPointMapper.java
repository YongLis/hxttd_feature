package com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.service.access.req.AccessPointQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.AccessPointEntity;
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
public interface AccessPointMapper extends BaseMapper<AccessPointEntity> {

    /**
     * 分页查询接入点
     */
    IPage<AccessPointEntity> pageQuery(Page<AccessPointEntity> page, @Param("req") AccessPointQueryReq req);


    /**
     * 根据项目ID查询接入点列表
     */
    List<AccessPointEntity> listByProjectId(Long projectId);

    /**
     * 根据编码查询接入点
     */
    AccessPointEntity getByCode(String code);
}
