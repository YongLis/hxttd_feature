package com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.service.dataStruct.req.DataStructQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.DataStructEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 数据集 Mapper
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Mapper
public interface DataStructMapper extends BaseMapper<DataStructEntity> {

    IPage<DataStructEntity> pageQuery(Page<DataStructEntity> page, @Param("req") DataStructQueryReq req);
}
