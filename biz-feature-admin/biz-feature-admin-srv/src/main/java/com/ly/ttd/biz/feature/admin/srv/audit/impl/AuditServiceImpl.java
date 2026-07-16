package com.ly.ttd.biz.feature.admin.srv.audit.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.AuditMapper;
import com.ly.ttd.biz.feature.admin.srv.audit.req.AuditApproveReq;
import com.ly.ttd.biz.feature.admin.srv.resource.ResourceOpFactory;
import com.ly.ttd.feature.admin.api.audit.AuditService;
import com.ly.ttd.feature.admin.api.consts.AuditStatusEnum;
import com.ly.ttd.feature.admin.api.dto.AuditDto;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 审核记录 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class AuditServiceImpl implements AuditService {
    @Resource
    private AuditMapper auditMapper;

    @Override
    public Long add(AuditDto dto) throws BizException {
        AuditEntity entity = new AuditEntity();
        BeanUtils.copyProperties(dto, entity);
        auditMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(AuditDto dto) throws BizException {
        AuditEntity entity = new AuditEntity();
        BeanUtils.copyProperties(dto, entity);
        auditMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        auditMapper.deleteById(id);
    }

    @Override
    public AuditDto queryById(Long id) throws BizException {
        AuditEntity entity = auditMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        AuditDto dto = new AuditDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public void submitAudit(Long id, String approveResult, String remark, String opUser) throws BizException {
        AuditEntity entity = auditMapper.selectById(id);
        if (entity == null) {
            throw new BizException("审核记录不存在");
        }

        if (!AuditStatusEnum.PENDING.getCode().equals(entity.getAuditStatus())) {
            throw new BizException("非审核记录状态，不允许重复审核");
        }

        AuditApproveReq req = new AuditApproveReq();
        req.setId(id);
        req.setAuditStatus(approveResult);
        req.setAuditComment(remark);
        req.setOpUser(opUser);
        ResourceOpFactory.getService(entity.getResourceType())
                .submitAudit(req);
    }
}
