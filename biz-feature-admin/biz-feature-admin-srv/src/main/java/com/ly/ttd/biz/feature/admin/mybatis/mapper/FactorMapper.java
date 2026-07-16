package com.ly.ttd.biz.feature.admin.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ly.ttd.biz.feature.admin.mybatis.entity.FactorEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * factor Mapper
 *
 * @author yong.li
 * @since 2026-05-27
 */
@Mapper
public interface FactorMapper extends BaseMapper<FactorEntity> {
    FactorEntity selectByResourceKey(String resourceKey);
}
