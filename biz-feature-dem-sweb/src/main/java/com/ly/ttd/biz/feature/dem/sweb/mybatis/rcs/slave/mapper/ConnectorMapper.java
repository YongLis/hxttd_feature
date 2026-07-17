package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.ConnectorEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.connector.req.ConnectorQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.connector.res.ConnectorQueryRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 连接器 Mapper
 *
 * @author yong.li
 * @since 2026-05-27
 */
@Mapper
public interface ConnectorMapper{

    /**
     * 分页查询连接器列表
     */
    List<ConnectorQueryRes> pageQuery(Page<ConnectorQueryRes> page, @Param("req") ConnectorQueryReq req);

    /**
     * 根据 resource_key 查询总数
     */
    long selectCountByKey(@Param("key") String key);

    ConnectorEntity selectById(Long id);

    /**
     * 查询全部
     */
    List<ConnectorEntity> selectByProjectId(@Param("projectId") String projectId);
}
