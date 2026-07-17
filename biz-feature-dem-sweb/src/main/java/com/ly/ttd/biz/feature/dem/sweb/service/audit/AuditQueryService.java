package com.ly.ttd.biz.feature.dem.sweb.service.audit;


import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.req.AuditQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.res.AuditDetail;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.res.AuditQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.AuditEntity;

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

    /**
     * 资源存在待审核记录检查
     */
    void waitAuditCheck(String resourceKey) throws BizException;

}
