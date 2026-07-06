package com.ly.ttd.biz.admin.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.admin.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.admin.srv.audit.req.AuditQueryReq;
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
}
