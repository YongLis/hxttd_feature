package com.ly.ttd.biz.feature.dem.sweb.service.audit.res;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity.ConnectorEntity;
import com.ly.ttd.feature.common.model.connector.JdbcConnectorModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yong.li
 * @since 2026/6/23 22:12
 */
@Data
public class JdbcConnectorAuditDetail extends AuditDetail {

    @Schema(description = "变更前JDBC连接器配置")
    private JdbcConnectorModel before;

    @Schema(description = "变更后JDBC连接器配置")
    private JdbcConnectorModel after;

    public static JdbcConnectorModel jsonConvert(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ConnectorEntity entity = JSON.parseObject(json, ConnectorEntity.class);
        return JdbcConnectorModel.convertResource(entity.getResourceJson());
    }

}
