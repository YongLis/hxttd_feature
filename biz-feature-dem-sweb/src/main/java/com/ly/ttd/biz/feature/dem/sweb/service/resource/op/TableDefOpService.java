package com.ly.ttd.biz.feature.dem.sweb.service.resource.op;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.AuditEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.res.AuditDetail;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.res.TableDefAuditDetail;
import com.ly.ttd.biz.feature.dem.sweb.service.resource.AbstractResourceOpService;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 数据表定义 资源操作服务（用于审核详情查询）
 *
 * @author yong.li
 * @since 2026-07-14
 */
@Slf4j
@Service
public class TableDefOpService extends AbstractResourceOpService {

    @Override
    public String getResourceType() {
        return FeatureResourceType.TABLE_DEF.getType();
    }

    @Override
    public AuditDetail getDetail(Long id) throws BizException {
        AuditEntity audit = auditMapper.selectById(id);
        if (audit == null) {
            throw new BizException("审核记录不存在");
        }

        TableDefAuditDetail detail = new TableDefAuditDetail();

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
            detail.setBefore(TableDefAuditDetail.jsonConvert(audit.getBeforeContent()));
            detail.setBeforeColumns(TableDefAuditDetail.jsonConvertColumns(audit.getBeforeContent()));
        }
        if (audit.getAfterContent() != null) {
            detail.setAfter(TableDefAuditDetail.jsonConvert(audit.getAfterContent()));
            detail.setAfterColumns(TableDefAuditDetail.jsonConvertColumns(audit.getAfterContent()));
        }
        return detail;
    }
}
