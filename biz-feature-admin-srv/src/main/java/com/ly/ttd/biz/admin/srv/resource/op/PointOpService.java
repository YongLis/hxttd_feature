package com.ly.ttd.biz.admin.srv.resource.op;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ly.ttd.biz.admin.consts.AuditStatusEnum;
import com.ly.ttd.biz.admin.consts.OperationTypeEnum;
import com.ly.ttd.biz.admin.consts.SequenceConst;
import com.ly.ttd.biz.admin.mybatis.entity.AccessPointEntity;
import com.ly.ttd.biz.admin.mybatis.entity.AccessPointParamEntity;
import com.ly.ttd.biz.admin.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.admin.mybatis.entity.DictEntity;
import com.ly.ttd.biz.admin.mybatis.mapper.AccessPointMapper;
import com.ly.ttd.biz.admin.mybatis.mapper.AccessPointParamMapper;
import com.ly.ttd.biz.admin.req.BaseRequest;
import com.ly.ttd.biz.admin.srv.access.req.AccessPointAddReq;
import com.ly.ttd.biz.admin.srv.access.req.AccessPointUpdateReq;
import com.ly.ttd.biz.admin.srv.access.res.ParamItem;
import com.ly.ttd.biz.admin.srv.audit.req.AuditApproveReq;
import com.ly.ttd.biz.admin.srv.audit.req.AuditReq;
import com.ly.ttd.biz.admin.srv.audit.res.AccessPointAuditDetail;
import com.ly.ttd.biz.admin.srv.audit.res.AuditDetail;
import com.ly.ttd.biz.admin.srv.dict.DictCodeService;
import com.ly.ttd.biz.admin.srv.dict.DictService;
import com.ly.ttd.biz.admin.srv.dict.req.DictAddReq;
import com.ly.ttd.biz.admin.srv.resource.AbstractResourceOpService;
import com.ly.ttd.biz.admin.srv.resource.req.ResourceChgReq;
import com.ly.ttd.biz.admin.srv.sequence.LocalSeqService;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 接入点资源操作服务
 *
 * @author yong.li
 * @since 2026/6/23 13:13
 */
@Service
@Slf4j
public class PointOpService extends AbstractResourceOpService {
    @Resource
    private AccessPointMapper accessPointMapper;
    @Resource
    private DictService dictService;
    @Resource
    private DictCodeService dictCodeService;
    @Resource
    private AccessPointParamMapper accessPointParamMapper;
    @Resource
    private LocalSeqService localSeqService;


    @Override
    public String getResourceType() {
        return FeatureResourceType.POINT.getType();
    }

    @Override
    public void add(BaseRequest req) throws FeatureBizException {
        AccessPointAddReq addReq = (AccessPointAddReq) req;
        String key = localSeqService.generateSeq(FeatureResourceType.POINT.getPrefix(), 5, SequenceConst.END_POINT);

        // 检查资源键唯一性
        QueryWrapper<AccessPointEntity> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("point_code", key);
        checkWrapper.eq("deleted", false);
        if (accessPointMapper.selectCount(checkWrapper) > 0) {
            throw new FeatureBizException("资源键已存在");
        }

        AccessPointEntity entity = new AccessPointEntity();
        entity.setCode(key);
        entity.setName(addReq.getName());
        entity.setVersion("V" + System.currentTimeMillis());
        for (ParamItem item : addReq.getParams()) {
            item.setVersion(entity.getVersion());
        }
        entity.setApiJson(JSON.toJSONString(addReq.getParams()));

        entity.setDeleted(false);

        addAudit(new AuditReq(key,
                getResourceType(),
                addReq.getName(),
                OperationTypeEnum.ADD.getCode(),
                null,
                JSON.toJSONString(entity)));

    }

    @Override
    public void update(BaseRequest req) throws FeatureBizException {
        AccessPointUpdateReq updateReq = (AccessPointUpdateReq) req;

        AccessPointEntity entity = accessPointMapper.selectById(updateReq.getId());
        if (entity == null) {
            throw new FeatureBizException("接入点不存在");
        }
        String beforeJson = JSON.toJSONString(entity);

        entity.setName(updateReq.getName());
        entity.setVersion("V" + System.currentTimeMillis());
        for (ParamItem item : updateReq.getParams()) {
            item.setVersion(entity.getVersion());
        }
        entity.setApiJson(JSON.toJSONString(updateReq.getParams()));

        addAudit(new AuditReq(entity.getCode(),
                getResourceType(),
                updateReq.getName(),
                OperationTypeEnum.UPDATE.getCode(),
                beforeJson, JSON.toJSONString(entity)));
    }

    private void saveDict(String pointCode, String name) {
        String dictCode = "meta_tag";
        String systemCode = "ttd";
        DictEntity dict = dictService.getDictCode(systemCode, dictCode);
        if (null == dict) {
            DictAddReq addReq = new DictAddReq();
            addReq.setSystemCode(systemCode);
            addReq.setDictCode(dictCode);
            addReq.setDictName("元字段分类");
            dictService.addDict(addReq);
            dict = dictService.getDictCode(systemCode, dictCode);
        }
        dictCodeService.addDictCode(dict.getId(), pointCode, name);
    }


    @Override
    @Transactional
    public void submitAudit(AuditApproveReq req) throws FeatureBizException {
        AuditEntity audit = checkAudit(req);
        audit.setAuditStatus(req.getAuditStatus());
        audit.setAuditComment(req.getAuditComment());
        if (req.getAuditStatus().equals(AuditStatusEnum.APPROVED.getCode())) {
            AccessPointEntity pointEntity = JSON.parseObject(audit.getAfterContent(), AccessPointEntity.class);

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
                accessPointMapper.insert(pointEntity);
                chgReq.setAfterVersion(pointEntity.getVersion());
                saveDict(pointEntity.getCode(), pointEntity.getName());

                saveParams(pointEntity.getCode(), pointEntity.getVersion(), pointEntity.apiConvert());
            } else if (audit.getOperationType().equals(OperationTypeEnum.DELETE.getCode())) {
                accessPointMapper.updateById(pointEntity);
                AccessPointEntity before = JSON.parseObject(audit.getBeforeContent(), AccessPointEntity.class);
                chgReq.setBeforeVersion(before.getVersion());
            } else {
                accessPointMapper.updateById(pointEntity);
                AccessPointEntity before = JSON.parseObject(audit.getBeforeContent(), AccessPointEntity.class);
                chgReq.setBeforeVersion(before.getVersion());

                // 删除旧版本记录
                deleteParamsByAccessPointCode(pointEntity.getCode(), before.getVersion());
                saveParams(pointEntity.getCode(), pointEntity.getVersion(), pointEntity.apiConvert());
            }
            addResourceChg(chgReq);
        }
        updateAuditStatus(audit);

    }

    @Override
    public AuditDetail getDetail(Long id) throws FeatureBizException {
        AuditEntity entity = auditMapper.selectById(id);

        AccessPointAuditDetail detail = new AccessPointAuditDetail();
        BeanUtils.copyProperties(entity, detail);

        if (StringUtils.isNotBlank(entity.getBeforeContent())) {
            detail.setBefore(AccessPointAuditDetail.jsonConvert(entity.getBeforeContent()));
        }
        if (StringUtils.isNotBlank(entity.getAfterContent())) {
            detail.setAfter(AccessPointAuditDetail.jsonConvert(entity.getAfterContent()));
        }
        return detail;
    }

    @Override
    public void delete(Long id) throws FeatureBizException {
        AccessPointEntity entity = accessPointMapper.selectById(id);
        if (entity == null) {
            throw new FeatureBizException("接入点不存在");
        }
        String beforeJson = JSON.toJSONString(entity);
        entity.setDeleted(true);
        String afterJson = JSON.toJSONString(entity);
        addAudit(new AuditReq(entity.getCode(), getResourceType(), entity.getName(),
                OperationTypeEnum.DELETE.getCode(), beforeJson, afterJson));
    }

    private void saveParams(String accessPointCode, String version, List<ParamItem> items) {
        if (CollectionUtils.isEmpty(items)) return;

        // 先录入一级编码
        Set<String> levelOneParams = new HashSet<>();
        items.stream().filter(t -> StringUtils.isBlank(t.getParentParamCode()))
                .forEach(f -> {
                    levelOneParams.add(f.getParamCode());
                    insertPointParamRecord(1, accessPointCode, version, f);
                });

        // 录入二级节点
        if (CollectionUtils.isNotEmpty(levelOneParams)) {
            Set<String> levelTwoParams = new HashSet<>();
            items.stream().filter(t -> levelOneParams.contains(t.getParentParamCode()))
                    .forEach(f -> {
                        levelTwoParams.add(f.getParamCode());
                        insertPointParamRecord(2, accessPointCode, version, f);
                    });

            if (CollectionUtils.isNotEmpty(levelTwoParams)) {
                items.stream().filter(t -> levelTwoParams.contains(t.getParentParamCode()))
                        .forEach(f -> {
                            insertPointParamRecord(3, accessPointCode, version, f);
                        });
            }
        }


    }

    private void insertPointParamRecord(int level, String accessPointCode, String version, ParamItem item) {
        AccessPointParamEntity e = new AccessPointParamEntity();
        e.setAccessPointCode(accessPointCode);
        e.setVersion(version);
        e.setParamName(item.getParamName());
        e.setParamCode(item.getParamCode());
        e.setParamType(item.getParamType());
        e.setRequired(item.getRequired() != null ? item.getRequired() : 0);
        e.setDefaultValue(item.getDefaultValue());
        e.setDescription(item.getDescription());
        e.setSortOrder(item.getSortOrder() != null ? item.getSortOrder() : null);
        e.setParentParamCode(item.getParentParamCode());
        e.setParamLevel(level);
        e.setDeleted(false);
        accessPointParamMapper.insert(e);
    }


    private void deleteParamsByAccessPointCode(String accessPointCode, String version) {
        LambdaQueryWrapper<AccessPointParamEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccessPointParamEntity::getAccessPointCode, accessPointCode);
        wrapper.eq(AccessPointParamEntity::getDeleted, false);
        UpdateWrapper<AccessPointParamEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("deleted", false);
        updateWrapper.eq("access_point_code", accessPointCode);
        updateWrapper.eq("version", version);
        updateWrapper.set("deleted", true);
        accessPointParamMapper.update(updateWrapper);
    }


}