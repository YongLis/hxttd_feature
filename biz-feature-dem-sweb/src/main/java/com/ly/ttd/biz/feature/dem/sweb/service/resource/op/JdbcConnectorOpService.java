package com.ly.ttd.biz.feature.dem.sweb.service.resource.op;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.res.AuditDetail;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.res.JdbcConnectorAuditDetail;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.ConnectorMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.resource.AbstractResourceOpService;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * JDBC 连接器操作服务
 *
 * @author yong.li
 * @since 2026/7/3
 */
@Service
@Slf4j
public class JdbcConnectorOpService extends AbstractResourceOpService {
    @Resource
    private ConnectorMapper connectorMapper;

    @Override
    public String getResourceType() {
        return FeatureResourceType.CONNECTOR_JDBC.getType();
    }


    @Override
    public AuditDetail getDetail(Long id) throws BizException {
        AuditEntity entity = auditMapper.selectById(id);
        JdbcConnectorAuditDetail detail = new JdbcConnectorAuditDetail();
        detail.setBefore(JdbcConnectorAuditDetail.jsonConvert(entity.getBeforeContent()));
        detail.setAfter(JdbcConnectorAuditDetail.jsonConvert(entity.getAfterContent()));
        return detail;
    }

}
