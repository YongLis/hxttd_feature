package com.ly.ttd.biz.feature.dem.sweb.service.resource.op;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.AuditEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.res.AuditDetail;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.res.PipeTaskAuditDetail;
import com.ly.ttd.biz.feature.dem.sweb.service.resource.AbstractResourceOpService;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 管道任务审核详情服务
 *
 * @author yong.li
 * @since 2026-07-22
 */
@Slf4j
@Service
public class PipeTaskOpService extends AbstractResourceOpService {

    @Override
    public String getResourceType() {
        return FeatureResourceType.PIPE_TASK.getType();
    }

    @Override
    public AuditDetail getDetail(Long id) throws BizException {
        AuditEntity audit = auditMapper.selectById(id);
        if (audit == null) {
            throw new BizException("审核记录不存在");
        }

        PipeTaskAuditDetail detail = new PipeTaskAuditDetail();

        detail.setId(audit.getId());
        detail.setResourceType(audit.getResourceType());
        detail.setResourceKey(audit.getResourceKey());
        detail.setResourceName(audit.getResourceName());
        detail.setAuditStatus(audit.getAuditStatus());
        detail.setOperationType(audit.getOperationType());
        detail.setAuditComment(audit.getAuditComment());
        detail.setSubmitUser(audit.getSubmitUser());
        detail.setSubmitTime(audit.getSubmitTime());
        detail.setAuditUser(audit.getAuditUser());
        detail.setAuditTime(audit.getAuditTime());

        if (audit.getBeforeContent() != null) {
            detail.setBefore(PipeTaskAuditDetail.jsonConvert(audit.getBeforeContent()));
        }
        if (audit.getAfterContent() != null) {
            detail.setAfter(PipeTaskAuditDetail.jsonConvert(audit.getAfterContent()));
        }
        return detail;
    }
}
