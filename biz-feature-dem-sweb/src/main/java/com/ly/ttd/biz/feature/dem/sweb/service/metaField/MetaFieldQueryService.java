package com.ly.ttd.biz.feature.dem.sweb.service.metaField;


import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.req.MetaFieldQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.res.MetaFieldQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.MetaFieldEntity;

import java.util.List;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/5/27 20:48
 */
public interface MetaFieldQueryService {


    /**
     * 分页查询元字段
     */
    PageResult<MetaFieldQueryRes> pageQuery(MetaFieldQueryReq req);


    /**
     * 查询所有元字段
     */
    Map<String, MetaFieldEntity> getByProjectId(Long projectId);

    MetaFieldEntity getById(Long id);

    List<MetaFieldEntity> getAllByProjectId(Long projectId);
}
