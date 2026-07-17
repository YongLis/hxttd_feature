package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.AuditEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.req.AuditQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 连接器审核 Mapper
 *
 * @author yong.li
 * @since 2026-05-30
 */
@Mapper
public interface AuditMapper {

    /**
     * 分页查询审核列表
     */
    List<AuditEntity> pageQuery(Page<AuditEntity> page, @Param("req") AuditQueryReq req);

    Long getWaitAuditCount(String resourceKey);

    AuditEntity selectById(Long id);

}
