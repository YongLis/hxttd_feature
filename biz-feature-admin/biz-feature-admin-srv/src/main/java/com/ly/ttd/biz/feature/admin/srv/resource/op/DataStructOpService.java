package com.ly.ttd.biz.feature.admin.srv.resource.op;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ly.ttd.biz.feature.admin.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.feature.admin.mybatis.entity.DataStructEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.DataStructMapper;
import com.ly.ttd.biz.feature.admin.srv.audit.req.AuditApproveReq;
import com.ly.ttd.biz.feature.admin.srv.audit.req.AuditReq;
import com.ly.ttd.biz.feature.admin.srv.resource.AbstractResourceOpService;
import com.ly.ttd.biz.feature.admin.srv.resource.req.ResourceChgReq;
import com.ly.ttd.feature.admin.api.consts.AuditStatusEnum;
import com.ly.ttd.feature.admin.api.consts.OperationTypeEnum;
import com.ly.ttd.feature.admin.api.dto.BaseDto;
import com.ly.ttd.feature.admin.api.dto.DataStructDto;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据集资源操作服务
 *
 * @author yong.li
 * @since 2026/6/23 13:13
 */
@Service
@Slf4j
public class DataStructOpService extends AbstractResourceOpService {
    @Resource
    private DataStructMapper dataStructMapper;


    @Override
    public String getResourceType() {
        return FeatureResourceType.DATA_STRUCT.getType();
    }

    @Override
    public void add(BaseDto req) throws FeatureBizException {
        DataStructDto addReq = (DataStructDto) req;
        String key = projectService.getResourceKey(addReq.getProjectId(), FeatureResourceType.DATA_STRUCT.getPrefix(), addReq.getResourceKey());
        addReq.setResourceKey(key);
        // 检查资源键唯一性
        LambdaQueryWrapper<DataStructEntity> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(DataStructEntity::getResourceKey, addReq.getResourceKey());
        checkWrapper.eq(DataStructEntity::getDeleted, false);
        if (dataStructMapper.selectCount(checkWrapper) > 0) {
            throw new FeatureBizException("资源键已存在");
        }

        DataStructEntity entity = new DataStructEntity();
        entity.setResourceKey(addReq.getResourceKey());
        entity.setResourceName(addReq.getResourceName());
        entity.setVersion("V" + System.currentTimeMillis());

        entity.setDeleted(false);

        addAudit(new AuditReq(addReq.getResourceKey(),
                getResourceType(),
                addReq.getResourceName(),
                OperationTypeEnum.ADD.getCode(),
                null,
                JSON.toJSONString(entity)));

    }

    @Override
    public void update(BaseDto req) throws FeatureBizException {
        DataStructDto updateReq = (DataStructDto) req;

        DataStructEntity entity = dataStructMapper.selectById(updateReq.getId());
        if (entity == null) {
            throw new FeatureBizException("数据集不存在");
        }
        String beforeJson = JSON.toJSONString(entity);

        entity.setResourceName(updateReq.getResourceName());
        entity.setVersion("V" + System.currentTimeMillis());

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
            DataStructEntity structEntity = JSON.parseObject(audit.getAfterContent(), DataStructEntity.class);

            ResourceChgReq chgReq = new ResourceChgReq(
                    audit.getResourceKey(),
                    audit.getResourceType(),
                    audit.getOperationType(),
                    null,
                    null,
                    audit.getBeforeContent(),
                    audit.getAfterContent(),
                    audit.getAuditStatus(),
                    audit.getSubmitUser());

            if (audit.getOperationType().equals(OperationTypeEnum.ADD.getCode())) {
                dataStructMapper.insert(structEntity);
                chgReq.setAfterVersion(structEntity.getVersion());
            } else {
                dataStructMapper.updateById(structEntity);
                DataStructEntity before = JSON.parseObject(audit.getBeforeContent(), DataStructEntity.class);
                chgReq.setBeforeVersion(before.getVersion());
            }
            addResourceChg(chgReq);
        }
        updateAuditStatus(audit);

    }

    @Override
    public void delete(Long id, String userName) throws FeatureBizException {
        DataStructEntity entity = dataStructMapper.selectById(id);
        if (entity == null) {
            throw new FeatureBizException("01", "数据集不存在");
        }
        String beforeJson = JSON.toJSONString(entity);
        entity.setDeleted(true);
        entity.setUptUser(userName);
        String afterJson = JSON.toJSONString(entity);
        addAudit(new AuditReq(entity.getResourceKey(), getResourceType(), entity.getResourceName(),
                OperationTypeEnum.DELETE.getCode(), beforeJson, afterJson));
    }
}