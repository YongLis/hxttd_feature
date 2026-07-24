package com.ly.ttd.biz.feature.dem.sweb.service.audit.res;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.PipeTask;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 管道任务审核详情
 *
 * @author yong.li
 * @since 2026-07-22
 */
@Data
public class PipeTaskAuditDetail extends AuditDetail {

    @Schema(description = "变更前管道任务配置")
    private PipeTask before;

    @Schema(description = "变更后管道任务配置")
    private PipeTask after;

    public static PipeTask jsonConvert(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return JSON.parseObject(json, PipeTask.class);
    }
}
