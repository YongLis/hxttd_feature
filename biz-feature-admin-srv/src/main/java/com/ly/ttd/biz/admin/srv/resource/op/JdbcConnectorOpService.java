package com.ly.ttd.biz.admin.srv.resource.op;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ly.ttd.biz.admin.consts.AuditStatusEnum;
import com.ly.ttd.biz.admin.consts.OperationTypeEnum;
import com.ly.ttd.biz.admin.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.admin.mybatis.entity.ConnectorEntity;
import com.ly.ttd.biz.admin.mybatis.mapper.ConnectorMapper;
import com.ly.ttd.biz.admin.req.BaseRequest;
import com.ly.ttd.biz.admin.srv.audit.req.AuditApproveReq;
import com.ly.ttd.biz.admin.srv.audit.req.AuditReq;
import com.ly.ttd.biz.admin.srv.audit.res.AuditDetail;
import com.ly.ttd.biz.admin.srv.audit.res.JdbcConnectorAuditDetail;
import com.ly.ttd.biz.admin.srv.connector.req.ConnectorAddReq;
import com.ly.ttd.biz.admin.srv.connector.req.ConnectorUpdateReq;
import com.ly.ttd.biz.admin.srv.resource.AbstractResourceOpService;
import com.ly.ttd.biz.admin.srv.resource.req.ResourceChgReq;
import com.ly.ttd.biz.admin.srv.user.LoginUser;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import com.ly.ttd.feature.common.model.connector.JdbcConnectorModel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void add(BaseRequest req) throws FeatureBizException {
        ConnectorAddReq addReq = (ConnectorAddReq) req;
        String key = projectService.getResourceKey(addReq.getProjectId(),
                FeatureResourceType.CONNECTOR_JDBC.getPrefix(), addReq.getResourceKey());
        addReq.setResourceKey(key);

        LambdaQueryWrapper<ConnectorEntity> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(ConnectorEntity::getResourceKey, addReq.getResourceKey());
        checkWrapper.eq(ConnectorEntity::getDeleted, false);
        if (connectorMapper.selectCount(checkWrapper) > 0) {
            throw new FeatureBizException("资源键已存在");
        }

        ConnectorEntity entity = new ConnectorEntity();
        entity.setResourceKey(addReq.getResourceKey());
        entity.setResourceName(addReq.getResourceName());
        entity.setVersion("V" + System.currentTimeMillis());
        entity.setProjectId(addReq.getProjectId());
        entity.setConnectorType(addReq.getConnectorType());
        entity.setDefaultValue(addReq.getDefaultValue());
        entity.setExceptionValue(addReq.getExceptionValue());
        entity.setTimeout(addReq.getTimeout());

        JdbcConnectorModel model = buildModel(addReq);
        entity.setResourceJson(JSON.toJSONString(model));
        entity.setCrtUser(LoginUser.getLoginUserName());
        entity.setDeleted(false);

        addAudit(new AuditReq(addReq.getResourceKey(),
                getResourceType(),
                addReq.getResourceName(),
                OperationTypeEnum.ADD.getCode(),
                null,
                JSON.toJSONString(entity)));
    }

    @Override
    public void update(BaseRequest req) throws FeatureBizException {
        ConnectorUpdateReq updateReq = (ConnectorUpdateReq) req;

        ConnectorEntity entity = connectorMapper.selectById(updateReq.getId());
        if (entity == null) {
            throw new FeatureBizException("不存在");
        }
        String beforeJson = JSON.toJSONString(entity);

        entity.setResourceName(updateReq.getResourceName());
        entity.setVersion("V" + System.currentTimeMillis());
        entity.setDefaultValue(updateReq.getDefaultValue());
        entity.setExceptionValue(updateReq.getExceptionValue());
        entity.setTimeout(updateReq.getTimeout());

        JdbcConnectorModel model = buildModel(updateReq);
        entity.setResourceJson(JSON.toJSONString(model));
        entity.setCrtUser(LoginUser.getLoginUserName());
        entity.setDeleted(false);

        addAudit(new AuditReq(entity.getResourceKey(),
                getResourceType(),
                updateReq.getResourceName(),
                OperationTypeEnum.UPDATE.getCode(),
                beforeJson, JSON.toJSONString(entity)));
    }

    @Override
    @Transactional
    public void submitAudit(AuditApproveReq req) throws FeatureBizException {
        AuditEntity audit = checkAudit(req);
        audit.setAuditStatus(req.getAuditStatus());
        audit.setAuditComment(req.getAuditComment());
        if (req.getAuditStatus().equals(AuditStatusEnum.APPROVED.getCode())) {
            ConnectorEntity fieldEntity = JSON.parseObject(audit.getAfterContent(), ConnectorEntity.class);

            ResourceChgReq chgReq = new ResourceChgReq(
                    audit.getResourceKey(),
                    audit.getResourceType(),
                    audit.getOperationType(),
                    null, null,
                    audit.getBeforeContent(),
                    audit.getAfterContent(),
                    audit.getAuditStatus(),
                    audit.getSubmitUser());

            if (audit.getOperationType().equals(OperationTypeEnum.ADD.getCode())) {
                connectorMapper.insert(fieldEntity);
                chgReq.setAfterVersion(fieldEntity.getVersion());
            } else {
                connectorMapper.updateById(fieldEntity);
                ConnectorEntity before = JSON.parseObject(audit.getBeforeContent(), ConnectorEntity.class);
                chgReq.setBeforeVersion(before.getVersion());
            }
            addResourceChg(chgReq);
        }
        updateAuditStatus(audit);
    }

    @Override
    public void delete(Long id) throws FeatureBizException {
        ConnectorEntity entity = connectorMapper.selectById(id);
        if (entity == null) {
            throw new FeatureBizException("连接器不存在");
        }
        String beforeJson = JSON.toJSONString(entity);
        entity.setDeleted(true);
        String afterJson = JSON.toJSONString(entity);
        addAudit(new AuditReq(entity.getResourceKey(), getResourceType(), entity.getResourceName(),
                OperationTypeEnum.DELETE.getCode(), beforeJson, afterJson));
    }

    @Override
    public AuditDetail getDetail(Long id) throws FeatureBizException {
        AuditEntity entity = auditMapper.selectById(id);
        JdbcConnectorAuditDetail detail = new JdbcConnectorAuditDetail();
        detail.setBefore(JdbcConnectorAuditDetail.jsonConvert(entity.getBeforeContent()));
        detail.setAfter(JdbcConnectorAuditDetail.jsonConvert(entity.getAfterContent()));
        return detail;
    }

    private JdbcConnectorModel buildModel(ConnectorAddReq req) {
        JdbcConnectorModel model = new JdbcConnectorModel();
        model.setDataSourceName(req.getDataSourceName());
        model.setSql(req.getSql());
        model.setFields(req.getFields());
        model.setCondition(req.getCondition());
        model.setConnectorType(req.getConnectorType());
        model.setProjectId(req.getProjectId());
        model.setResourceKey(req.getResourceKey());
        model.setResourceName(req.getResourceName());
        model.setVersion(req.getVersion());
        model.setReturnType(req.getReturnType());
        model.setTimeout(req.getTimeout());
        model.setDefaultValue(req.getDefaultValue());
        model.setExceptionValue(req.getExceptionValue());
        return model;
    }
}
