package com.ly.ttd.biz.admin.srv.metaField;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.mybatis.entity.MetaFieldEntity;
import com.ly.ttd.biz.admin.srv.metaField.req.MetaFieldQueryReq;
import com.ly.ttd.biz.admin.srv.metaField.res.MetaFieldQueryRes;

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
}
