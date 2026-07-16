package com.ly.ttd.feature.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 条件表达式（列条件，关联关系 AND）
 *
 * @author yong.li
 * @since 2026/5/17 08:33
 */
@Data
public class ColumnCondition {
    @Schema(description = "列条件列表(关联关系AND)")
    private List<ConditionMeta> columns;
}
