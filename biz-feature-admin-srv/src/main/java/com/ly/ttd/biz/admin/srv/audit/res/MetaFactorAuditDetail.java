package com.ly.ttd.biz.admin.srv.audit.res;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.biz.admin.mybatis.entity.FactorEntity;
import com.ly.ttd.biz.admin.srv.factor.res.MetaFactorDetailRes;
import com.ly.ttd.feature.common.model.factor.resource.MetaFactorResourceModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 指标审核详情
 *
 * @author yong.li
 * @since 2026/6/23 22:12
 */
@Data
public class MetaFactorAuditDetail extends AuditDetail {

    @Schema(description = "变更前指标配置")
    private MetaFactorDetailRes before;

    @Schema(description = "变更后指标配置")
    private MetaFactorDetailRes after;

    public static MetaFactorDetailRes jsonConvert(String json) {
        FactorEntity entity = JSON.parseObject(json, FactorEntity.class);

        MetaFactorDetailRes res = new MetaFactorDetailRes();
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

        MetaFactorResourceModel resourceModel = MetaFactorResourceModel.convertFromJson(entity.getResourceJson());
        res.setMetaFieldCode(resourceModel.getMetaFieldCode());
        res.setCrtUser(entity.getCrtUser());
        res.setCrtTime(entity.getCrtTime());
        return res;
    }

}
