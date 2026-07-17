package com.ly.ttd.biz.feature.dem.sweb.service.audit.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.AuditEntity;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper.AuditMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.AuditQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.req.AuditQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.res.AuditDetail;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.res.AuditQueryRes;
import com.ly.ttd.feature.common.enums.FeatureResultCodeEnum;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yong.li
 * @since 2026/6/5 09:27
 */
@Service
public class AuditQueryServiceImpl implements AuditQueryService {
    @Resource
    private AuditMapper auditMapper;

    @Override
    public PageResult<AuditQueryRes> pageQuery(AuditQueryReq req) {
        PageResult<AuditQueryRes> result = new PageResult<>();
        Page<AuditEntity> page = new Page<>(req.getCurrent(), req.getPageSize());
        List<AuditEntity> records = auditMapper.pageQuery(page, req);
        if (CollectionUtils.isNotEmpty(records)) {
            result.setData(records.stream().map(t -> {
                AuditQueryRes res = new AuditQueryRes();
                res.setId(t.getId());
                res.setResourceType(t.getResourceType());
                res.setResourceKey(t.getResourceKey());
                res.setResourceName(t.getResourceName());
                res.setAuditStatus(t.getAuditStatus());
                res.setOperationType(t.getOperationType());
                res.setAuditComment(t.getAuditComment());
                res.setSubmitUser(t.getSubmitUser());
                res.setAuditUser(t.getAuditUser());
                res.setSubmitTime(t.getSubmitTime());
                res.setAuditTime(t.getAuditTime());
                return res;
            }).collect(Collectors.toList()));
        }
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setCode(FeatureResultCodeEnum.SUCCESS.getCode());
        return result;
    }

    @Override
    public AuditDetail getDetail(Long id) {
        return null;
    }

    @Override
    public AuditEntity getById(Long id) {
        return auditMapper.selectById(id);
    }

    @Override
    public void waitAuditCheck(String resourceKey) throws BizException {
        Long count = auditMapper.getWaitAuditCount(resourceKey);
        if (count > 0) {
            throw new BizException("01", "存在待审核记录");
        }

    }
}
