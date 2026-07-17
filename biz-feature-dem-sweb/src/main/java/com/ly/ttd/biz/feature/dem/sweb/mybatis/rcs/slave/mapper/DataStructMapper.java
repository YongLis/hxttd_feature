package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.DataStructEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.dataStruct.req.DataStructQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据集 Mapper
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Mapper
public interface DataStructMapper {

    List<DataStructEntity> pageQuery(Page<DataStructEntity> page, @Param("req") DataStructQueryReq req);

    /**
     * 根据ID查询
     */
    DataStructEntity selectById(@Param("id") Long id);
    /**
     * 查询全部
     */
    List<DataStructEntity> selectAll();
    
    /**
     * 根据 resource_key 查询总数
     */
    long selectCountByKey(@Param("key") String key);
}
