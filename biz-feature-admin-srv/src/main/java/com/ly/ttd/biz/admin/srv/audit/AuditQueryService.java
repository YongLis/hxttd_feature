package com.ly.ttd.biz.admin.srv.audit;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.admin.srv.audit.req.AuditQueryReq;
import com.ly.ttd.biz.admin.srv.audit.res.AuditDetail;
import com.ly.ttd.biz.admin.srv.audit.res.AuditQueryRes;

/**
 * 特征审核服务
 *
 * @author yong.li
 * @since 2026-05-30
 */
public interface AuditQueryService {

    /**
     * 分页查询
     */
    PageResult<AuditQueryRes> pageQuery(AuditQueryReq req);


    AuditDetail getDetail(Long id);

    AuditEntity getById(Long id);

}
