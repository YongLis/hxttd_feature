package com.ly.ttd.biz.feature.admin.srv.resource.op;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ly.ttd.biz.feature.admin.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.feature.admin.mybatis.entity.PipeTask;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.PipeTaskMapper;
import com.ly.ttd.biz.feature.admin.srv.audit.req.AuditApproveReq;
import com.ly.ttd.biz.feature.admin.srv.audit.req.AuditReq;
import com.ly.ttd.biz.feature.admin.srv.resource.AbstractResourceOpService;
import com.ly.ttd.biz.feature.admin.srv.resource.req.ResourceChgReq;
import com.ly.ttd.feature.admin.api.consts.AuditStatusEnum;
import com.ly.ttd.feature.admin.api.consts.OperationTypeEnum;
import com.ly.ttd.feature.admin.api.dto.BaseDto;
import com.ly.ttd.feature.admin.api.dto.PipeTaskDto;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据管道任务 资源操作服务（接入统一审核）
 *
 * @author yong.li
 * @since 2026-07-14
 */
@Service
@Slf4j
public class PipeTaskOpService extends AbstractResourceOpService {

    @Resource
    private PipeTaskMapper pipeTaskMapper;

    @Override
    public String getResourceType() {
        return FeatureResourceType.PIPE_TASK.getType();
    }

    @Override
    public void add(BaseDto req) throws FeatureBizException {
        PipeTaskDto addReq = (PipeTaskDto) req;

        // 检查任务编码唯一性
        QueryWrapper<PipeTask> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("task_code", addReq.getTaskCode());
        checkWrapper.eq("deleted", false);
        if (pipeTaskMapper.selectCount(checkWrapper) > 0) {
            throw new FeatureBizException("任务编码已存在");
        }

        PipeTask entity = new PipeTask();
        entity.setPointCode(addReq.getPointCode());
        entity.setTaskCode(addReq.getTaskCode());
        entity.setTaskName(addReq.getTaskName());
        entity.setTableName(addReq.getTableName());
        entity.setKafkaTopic(addReq.getKafkaTopic());
        entity.setTaskStatus(addReq.getTaskStatus());
        entity.setCrtUser(addReq.getCrtUser());
        entity.setDeleted(false);

        addAudit(new AuditReq(entity.getTaskCode(),
                getResourceType(),
                entity.getTaskName(),
                OperationTypeEnum.ADD.getCode(),
                null,
                JSON.toJSONString(entity)));
    }

    @Override
    public void update(BaseDto req) throws FeatureBizException {
        PipeTaskDto updateReq = (PipeTaskDto) req;

        PipeTask entity = pipeTaskMapper.selectById(updateReq.getId());
        if (entity == null) {
            throw new FeatureBizException("管道任务不存在");
        }
        String beforeJson = JSON.toJSONString(entity);

        entity.setPointCode(updateReq.getPointCode());
        entity.setTaskCode(updateReq.getTaskCode());
        entity.setTaskName(updateReq.getTaskName());
        entity.setTableName(updateReq.getTableName());
        entity.setKafkaTopic(updateReq.getKafkaTopic());
        entity.setTaskStatus(updateReq.getTaskStatus());
        entity.setUptUser(updateReq.getUptUser());

        addAudit(new AuditReq(entity.getTaskCode(),
                getResourceType(),
                entity.getTaskName(),
                OperationTypeEnum.UPDATE.getCode(),
                beforeJson,
                JSON.toJSONString(entity)));
    }

    @Override
    @Transactional
    public void submitAudit(AuditApproveReq req) throws FeatureBizException {
        AuditEntity audit = checkAudit(req);
        audit.setAuditStatus(req.getAuditStatus());
        audit.setAuditComment(req.getAuditComment());

        if (req.getAuditStatus().equals(AuditStatusEnum.APPROVED.getCode())) {
            PipeTask entity = JSON.parseObject(audit.getAfterContent(), PipeTask.class);

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
                pipeTaskMapper.insert(entity);
            } else {
                pipeTaskMapper.updateById(entity);
            }
            addResourceChg(chgReq);
        }
        updateAuditStatus(audit);
    }

    @Override
    public void delete(Long id, String userName) throws FeatureBizException {
        // PipeTask主键为String类型，需转换
        String taskId = String.valueOf(id);
        PipeTask entity = pipeTaskMapper.selectById(taskId);
        if (entity == null) {
            throw new FeatureBizException("管道任务不存在");
        }
        String beforeJson = JSON.toJSONString(entity);
        entity.setDeleted(true);
        entity.setUptUser(userName);
        String afterJson = JSON.toJSONString(entity);

        addAudit(new AuditReq(entity.getTaskCode(),
                getResourceType(),
                entity.getTaskName(),
                OperationTypeEnum.DELETE.getCode(),
                beforeJson,
                afterJson));
    }
}
