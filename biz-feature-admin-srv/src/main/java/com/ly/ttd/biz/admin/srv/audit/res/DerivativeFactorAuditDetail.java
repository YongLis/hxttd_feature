package com.ly.ttd.biz.admin.srv.audit.res;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.biz.admin.mybatis.entity.FactorEntity;
import com.ly.ttd.biz.admin.srv.factor.res.DerivativeFactorDetailRes;
import com.ly.ttd.feature.common.model.factor.resource.DerivativeFactorResourceModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 指标审核详情
 *
 * @author yong.li
 * @since 2026/6/23 22:12
 */
@Data
public class DerivativeFactorAuditDetail extends AuditDetail {

    @Schema(description = "变更前指标配置")
    private DerivativeFactorDetailRes before;

    @Schema(description = "变更后指标配置")
    private DerivativeFactorDetailRes after;

    public static DerivativeFactorDetailRes jsonConvert(String json) {
        FactorEntity entity = JSON.parseObject(json, FactorEntity.class);

        DerivativeFactorDetailRes res = new DerivativeFactorDetailRes();
        res.setId(entity.getId());
        res.setResourceKey(entity.getResourceKey());
        res.setResourceName(entity.getResourceName());
        res.setVersion(entity.getVersion());
        res.setProjectId(entity.getProjectId());
        res.setFactorType(entity.getFactorType());
        res.setReturnType(entity.getReturnType());
        res.setDefaultValue(entity.getDefaultValue());
        res.setExceptionValue(entity.getExceptionValue());
        res.setTimeout(entity.getTimeout());

        DerivativeFactorResourceModel resourceModel = DerivativeFactorResourceModel.convertFromJson(entity.getResourceJson());

        res.setConnectorCode(resourceModel.getConnectorCode());
        res.setConditionScript(resourceModel.getConditionScript());
        res.setConnectorType(resourceModel.getConnectorType());
        res.setFactorCodes(resourceModel.getFactorCodes());
        res.setLanguage(resourceModel.getLanguage());
        res.setScript(resourceModel.getScript());
        res.setParams(resourceModel.getParams());


        res.setCrtUser(entity.getCrtUser());
        res.setCrtTime(entity.getCrtTime());
        return res;
    }

}
