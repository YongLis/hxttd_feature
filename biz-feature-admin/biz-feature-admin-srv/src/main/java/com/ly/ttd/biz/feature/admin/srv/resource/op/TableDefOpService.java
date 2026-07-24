package com.ly.ttd.biz.feature.admin.srv.resource.op;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ly.ttd.biz.feature.admin.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.feature.admin.mybatis.entity.TableDef;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.TableDefMapper;
import com.ly.ttd.biz.feature.admin.srv.audit.req.AuditApproveReq;
import com.ly.ttd.biz.feature.admin.srv.audit.req.AuditReq;
import com.ly.ttd.biz.feature.admin.srv.resource.AbstractResourceOpService;
import com.ly.ttd.biz.feature.admin.srv.resource.req.ResourceChgReq;
import com.ly.ttd.feature.admin.api.consts.AuditStatusEnum;
import com.ly.ttd.feature.admin.api.consts.OperationTypeEnum;
import com.ly.ttd.feature.admin.api.dto.BaseDto;
import com.ly.ttd.feature.admin.api.dto.TableDefDto;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据表定义 资源操作服务（接入统一审核）
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
@Slf4j
public class TableDefOpService extends AbstractResourceOpService {

    @Resource
    private TableDefMapper tableDefMapper;

    @Override
    public String getResourceType() {
        return FeatureResourceType.TABLE_DEF.getType();
    }

    @Override
    public void add(BaseDto req) throws FeatureBizException {
        TableDefDto addReq = (TableDefDto) req;

        // 检查表名唯一性
        QueryWrapper<TableDef> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("table_name", addReq.getTableName());
        checkWrapper.eq("deleted", false);
        if (tableDefMapper.selectCount(checkWrapper) > 0) {
            throw new FeatureBizException("表名已存在");
        }

        TableDef entity = new TableDef();
        entity.setTableName(addReq.getTableName());
        entity.setDataSource(addReq.getDataSource());
        entity.setCrtUser(addReq.getCrtUser());
        entity.setDeleted(false);


        addAudit(new AuditReq(entity.getTableName(),
                getResourceType(),
                entity.getTableName(),
                OperationTypeEnum.ADD.getCode(),
                null,
                JSON.toJSONString(entity), addReq.getCrtUser()));
    }

    @Override
    public void update(BaseDto req) throws FeatureBizException {
        TableDefDto updateReq = (TableDefDto) req;

        TableDef entity = tableDefMapper.selectById(updateReq.getId());
        if (entity == null) {
            throw new FeatureBizException("数据表定义不存在");
        }
        String beforeJson = JSON.toJSONString(entity);

        entity.setTableName(updateReq.getTableName());
        entity.setDataSource(updateReq.getDataSource());

        entity.setUptUser(updateReq.getUptUser());
        addAudit(new AuditReq(entity.getTableName(),
                getResourceType(),
                entity.getTableName(),
                OperationTypeEnum.UPDATE.getCode(),
                beforeJson,
                JSON.toJSONString(entity), updateReq.getUptUser()));
    }

    @Override
    @Transactional
    public void submitAudit(AuditApproveReq req) throws FeatureBizException {
        AuditEntity audit = checkAudit(req);
        audit.setAuditStatus(req.getAuditStatus());
        audit.setAuditComment(req.getAuditComment());

        if (req.getAuditStatus().equals(AuditStatusEnum.APPROVED.getCode())) {
            TableDef entity = JSON.parseObject(audit.getAfterContent(), TableDef.class);

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
                tableDefMapper.insert(entity);
            } else {
                tableDefMapper.updateById(entity);
            }
            addResourceChg(chgReq);
        }
        updateAuditStatus(audit);
    }

    @Override
    public void delete(Long id, String userName) throws FeatureBizException {
        // TableDef主键为String类型，需转换
        String tableId = String.valueOf(id);
        TableDef entity = tableDefMapper.selectById(tableId);
        if (entity == null) {
            throw new FeatureBizException("数据表定义不存在");
        }
        String beforeJson = JSON.toJSONString(entity);
        entity.setDeleted(true);
        entity.setUptUser(userName);
        String afterJson = JSON.toJSONString(entity);

        addAudit(new AuditReq(entity.getTableName(),
                getResourceType(),
                entity.getTableName(),
                OperationTypeEnum.DELETE.getCode(),
                beforeJson,
                afterJson,userName));
    }
}
