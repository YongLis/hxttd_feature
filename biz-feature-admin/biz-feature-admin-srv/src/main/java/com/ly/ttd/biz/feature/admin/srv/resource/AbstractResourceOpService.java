package com.ly.ttd.biz.feature.admin.srv.resource;

import com.ly.ttd.biz.feature.admin.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.feature.admin.mybatis.entity.ResourceChgEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.AuditMapper;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.ProjectMapper;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.ResourceChgMapper;
import com.ly.ttd.biz.feature.admin.srv.audit.req.AuditApproveReq;
import com.ly.ttd.biz.feature.admin.srv.audit.req.AuditReq;
import com.ly.ttd.biz.feature.admin.srv.resource.req.ResourceChgReq;
import com.ly.ttd.feature.admin.api.consts.AuditStatusEnum;
import com.ly.ttd.feature.admin.api.dto.BaseDto;
import com.ly.ttd.feature.admin.api.dto.FactorDependencyDto;
import com.ly.ttd.feature.admin.api.factor.FactorDependencyService;
import com.ly.ttd.feature.admin.api.project.ProjectService;
import com.ly.ttd.feature.admin.api.res.DependencyQueryRes;
import com.ly.ttd.feature.admin.api.res.Edge;
import com.ly.ttd.feature.admin.api.res.Node;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 资源操作服务抽象类
 *
 * @author yong.li
 * @since 2026/6/23 13:10
 */
@Service
public abstract class AbstractResourceOpService {
    @Resource
    protected ProjectService projectService;
    @Resource
    protected ProjectMapper projectMapper;
    @Resource
    protected AuditMapper auditMapper;
    @Resource
    protected ResourceChgMapper resourceChgMapper;
    @Resource
    private FactorDependencyService factorDependencyService;

    public abstract String getResourceType();


    /**
     * 添加资源
     */
    public abstract void add(BaseDto req) throws FeatureBizException;


    /**
     * 修改
     */
    public abstract void update(BaseDto req) throws FeatureBizException;


    /**
     * 审核通过/ 拒绝
     */
    public abstract void submitAudit(AuditApproveReq req) throws FeatureBizException;

    /**
     * 删除
     */
    public abstract void delete(Long id, String userName) throws FeatureBizException;

    protected void addFactorDependency(Long projectId, String parent, String parentType, List<String> childs, String user) {
        List<FactorDependencyDto> list = factorDependencyService.queryDependency(projectId, parent);
        if (CollectionUtils.isNotEmpty(list)) {
            factorDependencyService.removeDependency(projectId, parent);
        }
        factorDependencyService.addDependency(projectId, parent, parentType, childs, user);

    }


    protected DependencyQueryRes queryUpstreamDependency(Long projectId, String child) {
        DependencyQueryRes res = new DependencyQueryRes();
        List<FactorDependencyDto> list = factorDependencyService.queryUpstreamDependency(projectId, child);
        if (CollectionUtils.isNotEmpty(list)) {
            List<Node> nodes = new ArrayList<>();
            Set<String> nodeIds = new HashSet<>();
            for (FactorDependencyDto dep : list) {
                nodeIds.add(dep.getParent());
                nodeIds.add(dep.getChild());
            }
            nodes.addAll(nodeIds.stream().map(t -> {
                String resourceType = getResourceTypeByCode(projectId, t);
                return new Node(t, resourceType, t);
            }).collect(Collectors.toList()));
            res.setNodes(nodes);

            List<Edge> edges = list.stream().map(t -> new Edge(t.getId().toString(), t.getChild(), t.getParent()))
                    .collect(Collectors.toList());
            res.setEdges(edges);
        }

        return res;
    }


    protected void addAudit(AuditReq req) {
        AuditEntity record = new AuditEntity();
        record.setResourceType(req.getResourceType());
        record.setResourceKey(req.getResourceKey());
        record.setResourceName(req.getResourceName());
        record.setAuditStatus(AuditStatusEnum.PENDING.getCode());
        record.setOperationType(req.getOperationType());
        record.setBeforeContent(req.getBeforeJson());
        record.setAfterContent(req.getAfterJson());
        record.setSubmitUser(req.getUserName());
        record.setSubmitTime(new Date());
        auditMapper.insert(record);
    }

    protected AuditEntity checkAudit(AuditApproveReq req) throws FeatureBizException {
        AuditEntity record = auditMapper.selectById(req.getId());
        if (record == null) {
            throw new FeatureBizException("01", "审核记录不存在");
        }
        if (record.getAuditStatus().equals(AuditStatusEnum.APPROVED.getCode())
                || record.getAuditStatus().equals(AuditStatusEnum.REJECTED.getCode())) {
            throw new FeatureBizException("02", "已审核");
        }
        return record;
    }

    private String getResourceTypeByCode(Long projectId, String resourceKey) {
        String projectCode = getProjectCode(projectId);
        return FeatureResourceType.getTypeByCodePrefix(projectCode, resourceKey);
    }


    private String getProjectCode(Long projectId) {
        return projectMapper.selectById(projectId).getProjectCode();

    }

    protected void updateAuditStatus(AuditEntity audit) {
        auditMapper.updateById(audit);
    }

    /**
     * 添加资源变更记录
     */
    protected void addResourceChg(ResourceChgReq record) {
        ResourceChgEntity chgEntity = new ResourceChgEntity();
        chgEntity.setResourceKey(record.getResourceKey());
        chgEntity.setResourceType(record.getResourceType());
        chgEntity.setOperationType(record.getOperationType());
        chgEntity.setBeforeVersion(record.getBeforeVersion());
        chgEntity.setAfterVersion(record.getAfterVersion());
        chgEntity.setBeforeContent(record.getBeforeContent());
        chgEntity.setAfterContent(record.getAfterContent());
        chgEntity.setAuditStatus(record.getAuditStatus());
        chgEntity.setCrtTime(new Date());
        chgEntity.setCrtUser(record.getCrtUser());
        resourceChgMapper.insert(chgEntity);
    }


}
