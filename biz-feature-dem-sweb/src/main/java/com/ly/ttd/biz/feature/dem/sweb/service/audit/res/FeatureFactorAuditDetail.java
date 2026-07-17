package com.ly.ttd.biz.feature.dem.sweb.service.audit.res;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.biz.feature.dem.sweb.service.factor.res.FeatureFactorDetailRes;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.FactorEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 指标审核详情
 *
 * @author yong.li
 * @since 2026/6/23 22:12
 */
@Data
public class FeatureFactorAuditDetail extends AuditDetail {

    @Schema(description = "变更前指标配置")
    private FeatureFactorDetailRes before;

    @Schema(description = "变更后指标配置")
    private FeatureFactorDetailRes after;

    public static FeatureFactorDetailRes jsonConvert(String json) {
        FactorEntity entity = JSON.parseObject(json, FactorEntity.class);

        FeatureFactorDetailRes res = new FeatureFactorDetailRes();
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

        res.setFeatureCode(entity.getRefFeatureCode());

        res.setCrtUser(entity.getCrtUser());
        res.setCrtTime(entity.getCrtTime());
        return res;
    }

}
