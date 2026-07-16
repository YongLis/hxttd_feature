package com.ly.ttd.biz.feature.dem.sweb.service.connector.req;

import com.ly.ttd.feature.common.model.DataFieldModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 连接器添加请求
 *
 * @author yong.li
 * @since 2026-05-27
 */
@Data
public class HttpConnectorAddReq extends ConnectorAddReq {
    // ========== HTTP 连接器配置 ==========

    @Schema(description = "HTTP配置: 请求URL")
    private String url;

    @Schema(description = "HTTP配置: 请求方法(GET/POST/PUT/DELETE)")
    private String method;

    @Schema(description = "HTTP配置: 请求头")
    private Map<String, Object> header;

    @Schema(description = "HTTP配置: 请求参数")
    private Map<String, Object> param;

    @Schema(description = "参数列表")
    private List<DataFieldModel> fields;

    /**
     * 前置条件脚本
     */
    @Schema(description = "前置条件脚本")
    private String condition;

}


