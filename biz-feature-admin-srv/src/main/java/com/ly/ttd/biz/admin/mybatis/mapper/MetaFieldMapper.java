package com.ly.ttd.biz.admin.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.admin.mybatis.entity.MetaFieldEntity;
import com.ly.ttd.biz.admin.srv.metaField.req.MetaFieldQueryReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 元字段 Mapper
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Mapper
public interface MetaFieldMapper extends BaseMapper<MetaFieldEntity> {

    /**
     * 分页查询元字段
     *
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<MetaFieldEntity> pageQuery(Page<MetaFieldEntity> page, MetaFieldQueryReq req);


    List<MetaFieldEntity> getByProjectId(Long projectId);

    MetaFieldEntity selectByResourceKey(String resourceKey);
}
