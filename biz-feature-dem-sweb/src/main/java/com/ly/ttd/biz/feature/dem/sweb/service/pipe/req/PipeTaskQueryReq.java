package com.ly.ttd.biz.feature.dem.sweb.service.pipe.req;

import com.ly.ttd.base.result.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据管道任务 分页查询请求
 *
 * @author yong.li
 * @since 2026-07-22
 */
@Data
public class PipeTaskQueryReq extends PageQuery {

    @Schema(description = "任务编码")
    private String taskCode;

    @Schema(description = "任务名称（模糊查询）")
    private String taskName;
    @Schema(description = "接入点编码")
    private String pointCode;
    @Schema(description = "表名")
    private String tableName;
    @Schema(description = "topic")
    private String kafkaTopic;

    @Schema(description = "任务状态(0-开启,1-未开启)")
    private String taskStatus;

}
