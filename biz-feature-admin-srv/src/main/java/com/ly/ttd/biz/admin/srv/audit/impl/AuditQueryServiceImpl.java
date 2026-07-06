package com.ly.ttd.biz.admin.srv.audit.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.admin.mybatis.mapper.AuditMapper;
import com.ly.ttd.biz.admin.srv.audit.AuditQueryService;
import com.ly.ttd.biz.admin.srv.audit.req.AuditQueryReq;
import com.ly.ttd.biz.admin.srv.audit.res.AuditDetail;
import com.ly.ttd.biz.admin.srv.audit.res.AuditQueryRes;
import com.ly.ttd.feature.common.enums.FeatureResultCodeEnum;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

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
        auditMapper.pageQuery(page, req);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            result.setData(page.getRecords().stream().map(t -> {
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
}
