package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.DictCodeEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictCodeQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典键值 Mapper
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Mapper
public interface DictCodeMapper {

    List<DictCodeEntity> pageQuery(Page<DictCodeEntity> page, @Param("req") DictCodeQueryReq req);

    /**
     * 根据ID查询
     */
    DictCodeEntity selectById(@Param("id") Long id);
    /**
     * 查询全部
     */
    List<DictCodeEntity> selectByDictId(Long dictId);
    
    /**
     * 根据 dict_key 查询总数
     */
    long selectCountByKey(@Param("dictId") Long dictId,@Param("dictCode") String dictCode);
}
