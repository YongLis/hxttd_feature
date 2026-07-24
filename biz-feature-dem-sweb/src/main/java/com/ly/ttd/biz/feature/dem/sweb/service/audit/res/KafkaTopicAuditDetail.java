package com.ly.ttd.biz.feature.dem.sweb.service.audit.res;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.KafkaTopic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * HTTP 连接器审核详情
 *
 * @author yong.li
 * @since 2026/7/3
 */
@Data
public class KafkaTopicAuditDetail extends AuditDetail {

    @Schema(description = "变更前HTTP连接器配置")
    private KafkaTopic before;

    @Schema(description = "变更后HTTP连接器配置")
    private KafkaTopic after;

    public static KafkaTopic jsonConvert(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return JSON.parseObject(json, KafkaTopic.class);
    }
}
