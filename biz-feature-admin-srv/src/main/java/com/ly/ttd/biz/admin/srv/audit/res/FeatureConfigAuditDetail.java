package com.ly.ttd.biz.admin.srv.audit.res;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.biz.admin.mybatis.entity.FeatureConfigEntity;
import com.ly.ttd.biz.admin.srv.feature.res.FeatureConfigDetailRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 审核详情响应（结构化，包含解析后的配置对象）
 *
 * @author yong.li
 * @since 2026-05-30
 */
@Data
public class FeatureConfigAuditDetail extends AuditDetail {
    /**
     * 变更前的配置（结构化）
     */
    @Schema(description = "变更前的配置（结构化）")
    private FeatureConfigDetailRes before;

    /**
     * 变更后的配置（结构化）
     */
    @Schema(description = "变更后的配置（结构化）")
    private FeatureConfigDetailRes after;

    public static FeatureConfigDetailRes jsonConvert(String json) {
        FeatureConfigEntity config = JSON.parseObject(json, FeatureConfigEntity.class);
        return FeatureConfigDetailRes.fromEntity(config);

    }
}
