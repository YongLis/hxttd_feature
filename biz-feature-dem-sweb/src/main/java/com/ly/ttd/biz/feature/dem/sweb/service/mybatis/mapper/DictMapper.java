package com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.DictEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 字典配置 Mapper
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Mapper
public interface DictMapper extends BaseMapper<DictEntity> {

    IPage<DictEntity> pageQuery(Page<DictEntity> page, @Param("req") DictQueryReq req);
}
