package com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper;

import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.AccessPointParamEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 接入点请求入参配置 Mapper
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Mapper
public interface AccessPointParamMapper {
    /**
     * 根据ID查询
     */
    AccessPointParamEntity selectById(@Param("id") Long id);
    /**
     * 查询全部
     */
    List<AccessPointParamEntity> selectByAccessPointCode(@Param("accessPointCode") String accessPointCode, @Param("version") String version);
    
    /**
     * 根据 access_point_code 查询总数
     */
    long selectCountByKey(@Param("accessPointCode") String accessPointCode, @Param("paramCode") String paramCode);
}
