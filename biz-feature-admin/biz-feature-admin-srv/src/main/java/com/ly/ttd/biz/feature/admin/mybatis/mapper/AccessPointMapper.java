package com.ly.ttd.biz.feature.admin.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ly.ttd.biz.feature.admin.mybatis.entity.AccessPointEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 接入点 Mapper
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Mapper
public interface AccessPointMapper extends BaseMapper<AccessPointEntity> {
    /**
     * 根据项目ID查询接入点列表
     */
    List<AccessPointEntity> listByProjectId(Long projectId);

    /**
     * 根据编码查询接入点
     */
    AccessPointEntity getByCode(String code);
}
