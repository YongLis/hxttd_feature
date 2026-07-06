package com.ly.ttd.biz.admin.srv.resource;

import com.ly.ttd.biz.admin.consts.AuditStatusEnum;
import com.ly.ttd.biz.admin.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.admin.mybatis.entity.FactorDependencyEntity;
import com.ly.ttd.biz.admin.mybatis.entity.ResourceChgEntity;
import com.ly.ttd.biz.admin.mybatis.mapper.AuditMapper;
import com.ly.ttd.biz.admin.mybatis.mapper.ProjectMapper;
import com.ly.ttd.biz.admin.mybatis.mapper.ResourceChgMapper;
import com.ly.ttd.biz.admin.req.BaseRequest;
import com.ly.ttd.biz.admin.srv.audit.req.AuditApproveReq;
import com.ly.ttd.biz.admin.srv.audit.req.AuditReq;
import com.ly.ttd.biz.admin.srv.audit.res.AuditDetail;
import com.ly.ttd.biz.admin.srv.dependency.req.DependencyQueryReq;
import com.ly.ttd.biz.admin.srv.dependency.res.DependencyQueryRes;
import com.ly.ttd.biz.admin.srv.dependency.res.Edge;
import com.ly.ttd.biz.admin.srv.dependency.res.Node;
import com.ly.ttd.biz.admin.srv.factor.FactorDependencyQueryService;
import com.ly.ttd.biz.admin.srv.project.ProjectService;
import com.ly.ttd.biz.admin.srv.resource.req.ResourceChgReq;
import com.ly.ttd.biz.admin.srv.user.LoginUser;
import com.ly.ttd.consts.exception.BizException;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
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
    private FactorDependencyQueryService factorDependencyQueryService;

    public abstract String getResourceType();


    /**
     * 添加资源
     */
    public abstract void add(BaseRequest req) throws BizException;


    /**
     * 修改
     */
    public abstract void update(BaseRequest req) throws BizException;


    /**
     * 审核通过/ 拒绝
     */
    public abstract void submitAudit(AuditApproveReq req) throws BizException;

    /**
     * 审核详情
     */
    public abstract AuditDetail getDetail(Long id) throws BizException;


    /**
     * 删除
     */
    public abstract void delete(Long id) throws BizException;


    /**
     * 查询血缘
     */
    public DependencyQueryRes queryDependency(DependencyQueryReq req) throws BizException {
        throw new BizException("不支持血缘查询");
    }

    protected void addFactorDependency(Long projectId, String parent, String parentType, List<String> childs, String user) {
        List<FactorDependencyEntity> list = factorDependencyQueryService.queryDependency(projectId, parent);
        if (CollectionUtils.isNotEmpty(list)) {
            factorDependencyQueryService.removeDependency(projectId, parent);
        }
        factorDependencyQueryService.addDependency(projectId, parent, parentType, childs, user);

    }


    protected DependencyQueryRes queryUpstreamDependency(Long projectId, String child) {
        DependencyQueryRes res = new DependencyQueryRes();
        List<FactorDependencyEntity> list = factorDependencyQueryService.queryUpstreamDependency(projectId, child);
        if (CollectionUtils.isNotEmpty(list)) {
            List<Node> nodes = new ArrayList<>();
            Set<String> nodeIds = new HashSet<>();
            for (FactorDependencyEntity dep : list) {
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
        record.setSubmitUser(LoginUser.getLoginUserName());
        record.setSubmitTime(new Date());
        auditMapper.insert(record);
    }

    protected AuditEntity checkAudit(AuditApproveReq req) throws BizException {
        AuditEntity record = auditMapper.selectById(req.getId());
        if (record == null) {
            throw new BizException("01", "审核记录不存在");
        }
        if (record.getAuditStatus().equals(AuditStatusEnum.APPROVED.getCode())
                || record.getAuditStatus().equals(AuditStatusEnum.REJECTED.getCode())) {
            throw new BizException("02", "已审核");
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
