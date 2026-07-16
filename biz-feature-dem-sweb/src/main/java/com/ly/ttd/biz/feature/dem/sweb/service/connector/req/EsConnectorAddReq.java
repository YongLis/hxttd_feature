package com.ly.ttd.biz.feature.dem.sweb.service.connector.req;

import com.ly.ttd.feature.common.model.DataFieldModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 连接器添加请求
 *
 * @author yong.li
 * @since 2026-05-27
 */
@Data
public class EsConnectorAddReq extends ConnectorAddReq {

    // ========== ES 连接器配置 ==========

    @Schema(description = "ES配置: 索引端点")
    private String endpoint;

    @Schema(description = "ES配置: DSL查询语句")
    private String dsl;

    @Schema(description = "参数列表")
    private List<DataFieldModel> fields;

    /**
     * 前置条件脚本
     */
    @Schema(description = "前置条件脚本")
    private String condition;
}
