package com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.req.AuditQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.AuditEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 连接器审核 Mapper
 *
 * @author yong.li
 * @since 2026-05-30
 */
@Mapper
public interface AuditMapper extends BaseMapper<AuditEntity> {

    /**
     * 分页查询审核列表
     */
    IPage<AuditEntity> pageQuery(Page<AuditEntity> page, @Param("req") AuditQueryReq req);

    Long getWaitAuditCount(String resourceKey);
}
