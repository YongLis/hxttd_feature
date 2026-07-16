package com.ly.ttd.biz.feature.dem.sweb.service.resource;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.req.AuditApproveReq;
import com.ly.ttd.biz.feature.dem.sweb.service.audit.res.AuditDetail;
import com.ly.ttd.biz.feature.dem.sweb.service.dependency.req.DependencyQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.FactorDependencyQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.FactorDependencyEntity;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.AuditMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.ProjectMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.mapper.ResourceChgMapper;
import com.ly.ttd.feature.admin.api.consts.AuditStatusEnum;
import com.ly.ttd.feature.admin.api.res.DependencyQueryRes;
import com.ly.ttd.feature.admin.api.res.Edge;
import com.ly.ttd.feature.admin.api.res.Node;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    protected ResourceChgMapper resourceChgMapper;
    @Resource
    protected AuditMapper auditMapper;
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private FactorDependencyQueryService factorDependencyQueryService;

    public abstract String getResourceType();


    /**
     * 审核详情
     */
    public abstract AuditDetail getDetail(Long id) throws BizException;

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
}
