package com.ly.ttd.biz.feature.admin.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ly.ttd.biz.feature.admin.mybatis.entity.MetaFieldEntity;
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

    List<MetaFieldEntity> getByProjectId(Long projectId);

    MetaFieldEntity selectByResourceKey(String resourceKey);
}
