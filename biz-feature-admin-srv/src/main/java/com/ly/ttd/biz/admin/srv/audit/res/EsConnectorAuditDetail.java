package com.ly.ttd.biz.admin.srv.audit.res;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.biz.admin.mybatis.entity.ConnectorEntity;
import com.ly.ttd.feature.common.model.connector.ElasticsearchConnectorModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yong.li
 * @since 2026/6/23 22:12
 */
@Data
public class EsConnectorAuditDetail extends AuditDetail {

    @Schema(description = "变更前ES连接器配置")
    private ElasticsearchConnectorModel before;

    @Schema(description = "变更后ES连接器配置")
    private ElasticsearchConnectorModel after;

    public static ElasticsearchConnectorModel jsonConvert(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ConnectorEntity entity = JSON.parseObject(json, ConnectorEntity.class);
        return ElasticsearchConnectorModel.convertResource(entity.getResourceJson());
    }

}
