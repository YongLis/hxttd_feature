package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.DictEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典配置 Mapper
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Mapper
public interface DictMapper{

    List<DictEntity> pageQuery(Page<DictEntity> page, @Param("req") DictQueryReq req);

    /**
     * 根据ID查询
     */
    DictEntity selectById(@Param("id") Long id);


    
    /**
     * 根据 dict_code 查询总数
     */
    long selectCountByKey(@Param("key") String key);
}
