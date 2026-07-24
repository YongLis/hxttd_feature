package com.ly.ttd.biz.feature.admin.srv.resource.op;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ly.ttd.biz.feature.admin.mybatis.entity.AccessPointEntity;
import com.ly.ttd.biz.feature.admin.mybatis.entity.AccessPointParamEntity;
import com.ly.ttd.biz.feature.admin.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.AccessPointMapper;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.AccessPointParamMapper;
import com.ly.ttd.biz.feature.admin.srv.audit.req.AuditApproveReq;
import com.ly.ttd.biz.feature.admin.srv.audit.req.AuditReq;
import com.ly.ttd.biz.feature.admin.srv.resource.AbstractResourceOpService;
import com.ly.ttd.biz.feature.admin.srv.resource.req.ResourceChgReq;
import com.ly.ttd.feature.admin.api.consts.AuditStatusEnum;
import com.ly.ttd.feature.admin.api.consts.OperationTypeEnum;
import com.ly.ttd.feature.admin.api.consts.SequenceConst;
import com.ly.ttd.feature.admin.api.dict.DictCodeService;
import com.ly.ttd.feature.admin.api.dict.DictService;
import com.ly.ttd.feature.admin.api.dto.AccessPointDto;
import com.ly.ttd.feature.admin.api.dto.AccessPointParamDto;
import com.ly.ttd.feature.admin.api.dto.BaseDto;
import com.ly.ttd.feature.admin.api.dto.DictDto;
import com.ly.ttd.feature.admin.api.sequence.SequenceService;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
    private SequenceService sequenceService;


    @Override
    public String getResourceType() {
        return FeatureResourceType.POINT.getType();
    }

    @Override
    public void add(BaseDto req) throws FeatureBizException {
        AccessPointDto addReq = (AccessPointDto) req;
        String key = sequenceService.generateSeq(FeatureResourceType.POINT.getPrefix(), 5, SequenceConst.END_POINT);

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
        entity.setCrtUser(addReq.getCrtUser());
        entity.setVersion("V" + System.currentTimeMillis());
        for (AccessPointParamDto item : addReq.getParamItems()) {
            item.setVersion(entity.getVersion());
        }
        entity.setApiJson(JSON.toJSONString(addReq.getParamItems()));

        entity.setDeleted(false);

        addAudit(new AuditReq(key,
                getResourceType(),
                addReq.getName(),
                OperationTypeEnum.ADD.getCode(),
                null,
                JSON.toJSONString(entity), addReq.getCrtUser()));

    }

    @Override
    public void update(BaseDto req) throws FeatureBizException {
        AccessPointDto updateReq = (AccessPointDto) req;

        AccessPointEntity entity = accessPointMapper.selectById(updateReq.getId());
        if (entity == null) {
            throw new FeatureBizException("接入点不存在");
        }
        String beforeJson = JSON.toJSONString(entity);

        entity.setName(updateReq.getName());
        entity.setVersion("V" + System.currentTimeMillis());
        for (AccessPointParamDto item : updateReq.getParamItems()) {
            item.setVersion(entity.getVersion());
        }
        entity.setApiJson(JSON.toJSONString(updateReq.getParamItems()));
        entity.setUptUser(updateReq.getUptUser());

        addAudit(new AuditReq(entity.getCode(),
                getResourceType(),
                updateReq.getName(),
                OperationTypeEnum.UPDATE.getCode(),
                beforeJson, JSON.toJSONString(entity), updateReq.getUptUser()));
    }

    private void saveDict(String pointCode, String name) {
        String dictCode = "meta_tag";
        String systemCode = "ttd";
        DictDto dict = dictService.getDictCode(systemCode, dictCode);
        if (null == dict) {
            DictDto addReq = new DictDto();
            addReq.setSystemCode(systemCode);
            addReq.setDictCode(dictCode);
            addReq.setDictName("元字段分类");
            dictService.add(addReq);
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
    public void delete(Long id, String userName) throws FeatureBizException {
        AccessPointEntity entity = accessPointMapper.selectById(id);
        if (entity == null) {
            throw new FeatureBizException("接入点不存在");
        }
        String beforeJson = JSON.toJSONString(entity);
        entity.setDeleted(true);
        entity.setUptUser(userName);
        String afterJson = JSON.toJSONString(entity);
        addAudit(new AuditReq(entity.getCode(), getResourceType(), entity.getName(),
                OperationTypeEnum.DELETE.getCode(), beforeJson, afterJson,userName));
    }

    private void saveParams(String accessPointCode, String version, List<AccessPointParamDto> items) {
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

    private void insertPointParamRecord(int level, String accessPointCode, String version, AccessPointParamDto item) {
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