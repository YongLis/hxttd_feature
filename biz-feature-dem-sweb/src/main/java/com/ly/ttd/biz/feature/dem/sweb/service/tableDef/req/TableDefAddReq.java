package com.ly.ttd.biz.feature.dem.sweb.service.tableDef.req;

import com.ly.ttd.base.result.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 数据表定义 添加请求
 *
 * @author yong.li
 * @since 2026-07-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TableDefAddReq extends BaseRequest {

    @NotBlank(message = "表名不能为空")
    @Schema(description = "表名")
    private String tableName;

    @NotBlank(message = "库名不能为空")
    @Schema(description = "库名")
    private String dataSource;

    @Schema(description = "关联topic")
    private String topic;

    @Schema(description = "字段列表")
    private List<TableColumnReq> columns;

    @Schema(description = "项目ID")
    private Long projectId;
}
