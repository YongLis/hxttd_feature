package com.ly.ttd.biz.feature.dem.sweb.service.audit.res;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.TableDef;
import com.ly.ttd.biz.feature.dem.sweb.service.tableDef.res.TableColumnDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据表定义 审核详情
 *
 * @author yong.li
 * @since 2026-07-14
 */
@Data
public class TableDefAuditDetail extends AuditDetail {

    @Schema(description = "变更前表定义")
    private TableDef before;

    @Schema(description = "变更后表定义")
    private TableDef after;

    @Schema(description = "变更前字段列表")
    private List<TableColumnDetail> beforeColumns;

    @Schema(description = "变更后字段列表")
    private List<TableColumnDetail> afterColumns;

    public static TableDef jsonConvert(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return JSON.parseObject(json, TableDef.class);
    }

    public static List<TableColumnDetail> jsonConvertColumns(String json) {
        if (StringUtils.isBlank(json)) {
            return Collections.emptyList();
        }
        JSONObject obj = JSON.parseObject(json);
        JSONArray arr = obj.getJSONArray("columns");
        if (CollectionUtils.isEmpty(arr)) {
            return Collections.emptyList();
        }
        return arr.stream()
                .map(item -> JSON.toJavaObject((JSONObject) item, TableColumnDetail.class))
                .collect(Collectors.toList());
    }
}
