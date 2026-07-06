package com.ly.ttd.biz.admin.controller;

import com.ly.ttd.biz.admin.common.PageResult;
import com.ly.ttd.biz.admin.common.Result;
import com.ly.ttd.biz.admin.mybatis.entity.AuditEntity;
import com.ly.ttd.biz.admin.srv.audit.AuditQueryService;
import com.ly.ttd.biz.admin.srv.audit.req.AuditApproveReq;
import com.ly.ttd.biz.admin.srv.audit.req.AuditQueryReq;
import com.ly.ttd.biz.admin.srv.audit.res.*;
import com.ly.ttd.biz.admin.srv.resource.ResourceOpFactory;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 特征审核 Controller
 *
 * @author yong.li
 * @since 2026-05-30
 */
@RestController
@RequestMapping("/api/audit")
@Tag(name = "审核管理", description = "特征/指标/连接器/接入点/元字段审批流程管理接口")
@Slf4j
public class AuditController {
    @Resource
    private AuditQueryService auditQueryService;

    @Operation(summary = "审核列表分页查询", description = "根据资源类型、状态等条件分页查询审核记录")
    @PostMapping("/page")
    public PageResult<AuditQueryRes> page(@RequestBody AuditQueryReq req) {
        return auditQueryService.pageQuery(req);
    }

    @Operation(summary = "获取接入点审核详情")
    @GetMapping("/getPointAuditDetail")
    public Result<AccessPointAuditDetail> getPointAuditDetail(@Parameter(description = "审核记录ID") @RequestParam Long id) {
        try {
            AccessPointAuditDetail detail = (AccessPointAuditDetail) ResourceOpFactory.getService(FeatureResourceType.POINT.getType())
                    .getDetail(id);
            return Result.success(detail);
        } catch (FeatureBizException e1) {
            return Result.error(e1.getMessage());
        } catch (Exception e) {
            log.error("get point audit detail error, id={}", id, e);
        }
        return Result.error("get point audit detail error");
    }

    @Operation(summary = "获取元字段审核详情")
    @GetMapping("/getMetaAuditDetail")
    public Result<MetaFieldAuditDetail> getMetaAuditDetail(@Parameter(description = "审核记录ID") @RequestParam Long id) {
        try {
            MetaFieldAuditDetail detail = (MetaFieldAuditDetail) ResourceOpFactory.getService(FeatureResourceType.META_FIELD.getType())
                    .getDetail(id);
            return Result.success(detail);
        } catch (FeatureBizException e1) {
            return Result.error(e1.getMessage());
        } catch (Exception e) {
            log.error("get meta audit detail error, id={}", id, e);
        }
        return Result.error("get meta audit detail error");
    }

    @Operation(summary = "获取特征配置审核详情")
    @GetMapping("/getFeatureAuditDetail")
    public Result<FeatureConfigAuditDetail> getFeatureAuditDetail(@Parameter(description = "审核记录ID") @RequestParam Long id) {
        try {
            FeatureConfigAuditDetail detail = (FeatureConfigAuditDetail) ResourceOpFactory.getService(FeatureResourceType.FEATURE_CONFIG.getType())
                    .getDetail(id);
            return Result.success(detail);
        } catch (FeatureBizException e1) {
            return Result.error(e1.getMessage());
        } catch (Exception e) {
            log.error("get feature config audit detail error, id={}", id, e);
        }
        return Result.error("get feature config audit detail error");
    }

    @Operation(summary = "元字段指标审核详情")
    @GetMapping("/getMetaFactorAuditDetail")
    public Result<MetaFactorAuditDetail> getFactorAuditDetail(@Parameter(description = "审核记录ID") @RequestParam Long id) {
        try {
            MetaFactorAuditDetail detail = (MetaFactorAuditDetail) ResourceOpFactory.getService(FeatureResourceType.FACTOR_META.getType())
                    .getDetail(id);
            return Result.success(detail);
        } catch (FeatureBizException e1) {
            return Result.error(e1.getMessage());
        } catch (Exception e) {
            log.error("get factor audit detail error, id={}", id, e);
        }
        return Result.error("get factor audit detail error");
    }

    @Operation(summary = "衍生指标审核详情")
    @GetMapping("/getDerivativeFactorAuditDetail")
    public Result<DerivativeFactorAuditDetail> getDerivativeFactorAuditDetail(@Parameter(description = "审核记录ID") @RequestParam Long id) {
        try {
            DerivativeFactorAuditDetail detail = (DerivativeFactorAuditDetail) ResourceOpFactory.getService(FeatureResourceType.FACTOR_DERIVATIVE.getType())
                    .getDetail(id);
            return Result.success(detail);
        } catch (FeatureBizException e1) {
            return Result.error(e1.getMessage());
        } catch (Exception e) {
            log.error("get factor audit detail error, id={}", id, e);
        }
        return Result.error("get factor audit detail error");
    }

    @Operation(summary = "实时特征指标审核详情")
    @GetMapping("/getFeatureFactorAuditDetail")
    public Result<FeatureFactorAuditDetail> getFeatureFactorAuditDetail(@Parameter(description = "审核记录ID") @RequestParam Long id) {
        try {
            FeatureFactorAuditDetail detail = (FeatureFactorAuditDetail) ResourceOpFactory.getService(FeatureResourceType.FACTOR_FEATURE.getType())
                    .getDetail(id);
            return Result.success(detail);
        } catch (FeatureBizException e1) {
            return Result.error(e1.getMessage());
        } catch (Exception e) {
            log.error("get factor audit detail error, id={}", id, e);
        }
        return Result.error("get factor audit detail error");
    }


    @Operation(summary = "JDBC连接器审核详情")
    @GetMapping("/getJdbcConnectorAuditDetail")
    public Result<JdbcConnectorAuditDetail> getJdbcConnectorAuditDetail(@Parameter(description = "审核记录ID") @RequestParam Long id) {
        try {
            JdbcConnectorAuditDetail detail = (JdbcConnectorAuditDetail) ResourceOpFactory.getService(FeatureResourceType.CONNECTOR_JDBC.getType())
                    .getDetail(id);
            return Result.success(detail);
        } catch (FeatureBizException e1) {
            return Result.error(e1.getMessage());
        } catch (Exception e) {
            log.error("get jdbc connector audit detail error, id={}", id, e);
        }
        return Result.error("get jdbc connector audit detail error");
    }

    @Operation(summary = "ES连接器审核详情")
    @GetMapping("/getEsConnectorAuditDetail")
    public Result<EsConnectorAuditDetail> getEsConnectorAuditDetail(@Parameter(description = "审核记录ID") @RequestParam Long id) {
        try {
            EsConnectorAuditDetail detail = (EsConnectorAuditDetail) ResourceOpFactory.getService(FeatureResourceType.CONNECTOR_ES.getType())
                    .getDetail(id);
            return Result.success(detail);
        } catch (FeatureBizException e1) {
            return Result.error(e1.getMessage());
        } catch (Exception e) {
            log.error("get es connector audit detail error, id={}", id, e);
        }
        return Result.error("get es connector audit detail error");
    }

    @Operation(summary = "HTTP连接器审核详情")
    @GetMapping("/getHttpConnectorAuditDetail")
    public Result<HttpConnectorAuditDetail> getHttpConnectorAuditDetail(@Parameter(description = "审核记录ID") @RequestParam Long id) {
        try {
            HttpConnectorAuditDetail detail = (HttpConnectorAuditDetail) ResourceOpFactory.getService(FeatureResourceType.CONNECTOR_HTTP.getType())
                    .getDetail(id);
            return Result.success(detail);
        } catch (FeatureBizException e1) {
            return Result.error(e1.getMessage());
        } catch (Exception e) {
            log.error("get http connector audit detail error, id={}", id, e);
        }
        return Result.error("get http connector audit detail error");
    }

    @Operation(summary = "提交审核结果", description = "审核通过(APPROVED)或驳回(REJECTED)")
    @PostMapping("/submit")
    public Result<Boolean> submit(@Valid @RequestBody AuditApproveReq req) {
        try {
            AuditEntity entity = auditQueryService.getById(req.getId());
            ResourceOpFactory.getService(entity.getResourceType())
                    .submitAudit(req);
            return Result.success(true);
        } catch (FeatureBizException e1) {
            return Result.error(e1.getMessage());
        } catch (Exception e) {
            log.error("submit audit error, id={}", req.getId(), e);
        }
        return Result.error("submit audit error");
    }
}
