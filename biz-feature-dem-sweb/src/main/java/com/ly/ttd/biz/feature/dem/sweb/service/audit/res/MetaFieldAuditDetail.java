package com.ly.ttd.biz.feature.dem.sweb.service.audit.res;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.res.MetaFieldQueryRes;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.MetaFieldEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author yong.li
 * @since 2026/6/23 22:12
 */
@Data
public class MetaFieldAuditDetail extends AuditDetail {

    @Schema(description = "变更前元字段配置")
    private MetaFieldQueryRes before;

    @Schema(description = "变更后元字段配置")
    private MetaFieldQueryRes after;

    public static MetaFieldQueryRes jsonConvert(String json) {
        MetaFieldEntity entity = JSON.parseObject(json, MetaFieldEntity.class);

        MetaFieldQueryRes res = new MetaFieldQueryRes();
        res.setId(entity.getId());
        res.setResourceKey(entity.getResourceKey());
        res.setResourceName(entity.getResourceName());
        res.setVersion(entity.getVersion());
        res.setProjectId(entity.getProjectId());
        res.setLanguage(entity.getLanguage());
        res.setScript(entity.getScript());
        res.setReturnType(entity.getReturnType());
        res.setDefaultValue(entity.getDefaultValue());
        res.setExceptionValue(entity.getExceptionValue());
        res.setCategoryTag(entity.getCategoryTag());
        res.setCrtUser(entity.getCrtUser());
        res.setCrtTime(entity.getCrtTime());
        return res;

    }

}
