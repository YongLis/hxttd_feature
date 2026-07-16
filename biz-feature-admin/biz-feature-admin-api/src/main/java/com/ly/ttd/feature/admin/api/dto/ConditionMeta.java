package com.ly.ttd.feature.admin.api.dto;

import com.ly.ttd.feature.common.enums.VelocityValueTypeEnum;
import com.ly.ttd.feature.common.model.DataFieldModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 条件表达式
 *
 * @author yong.li
 * @since 2026/5/17 08:33
 */
@Data
public class ConditionMeta {
    /**
     * 左侧字段
     */
    @Schema(description = "左侧字段(元字段编码)", example = "META_USER_ID")
    private String left;

    /**
     * 操作符
     */
    @Schema(description = "操作符: EQ/NE/GT/GE/LT/LE/IN/LIKE", example = "EQ")
    private String op;
    /**
     * 右侧数据类型：动态：DYNAMIC, 固定值 FIXED
     */
    @Schema(description = "右侧数据类型: DYNAMIC-动态值, FIXED-固定值", example = "FIXED")
    private String rightType;

    @Schema(description = "右侧值")
    private String right;


    public DataFieldModel getLeftField(Map<String, MetaFieldDto> fields) {
        return new DataFieldModel(left, fields.get(left).getResourceName(), fields.get(left).getReturnType());
    }


    public DataFieldModel getRightField(Map<String, MetaFieldDto> fields) {
        if (VelocityValueTypeEnum.DYNAMIC_VALUE.getCode().equals(rightType)) {
            return new DataFieldModel(right, fields.get(right).getResourceName(), fields.get(right).getReturnType());
        }
        return null;
    }


}
