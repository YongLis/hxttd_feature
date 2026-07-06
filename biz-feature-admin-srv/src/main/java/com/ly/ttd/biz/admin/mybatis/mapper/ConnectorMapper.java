package com.ly.ttd.biz.admin.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.admin.mybatis.entity.ConnectorEntity;
import com.ly.ttd.biz.admin.srv.connector.req.ConnectorQueryReq;
import com.ly.ttd.biz.admin.srv.connector.res.ConnectorQueryRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 连接器 Mapper
 *
 * @author yong.li
 * @since 2026-05-27
 */
@Mapper
public interface ConnectorMapper extends BaseMapper<ConnectorEntity> {

    /**
     * 分页查询连接器列表
     */
    IPage<ConnectorQueryRes> pageQuery(Page<ConnectorQueryRes> page, @Param("req") ConnectorQueryReq req);
}
