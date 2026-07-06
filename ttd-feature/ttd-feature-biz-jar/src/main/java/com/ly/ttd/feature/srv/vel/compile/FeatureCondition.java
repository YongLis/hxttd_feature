package com.ly.ttd.feature.srv.vel.compile;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 实时特征配置数据结构
 *
 * @author yong.li
 * @since 2026/4/3 09:28
 */
@Data
public class FeatureCondition {
    /**
     * 条件列表（默认条件组之间 OR）
     */
    private List<RouterCondition> conditions = new ArrayList<>();

    public FeatureCondition() {
    }


    public FeatureCondition(List<RouterCondition> conditions) {
        this.conditions = conditions;
    }

    public void addCondition(DataFrame field, String op, DataFrame value, String valueType, String fixValue) {
        RouterCondition routerCondition = new RouterCondition();
        routerCondition.setField(field);
        routerCondition.setOp(op);
        routerCondition.setValue(value);
        routerCondition.setValueType(valueType);
        routerCondition.setFixValue(fixValue);
        this.conditions.add(routerCondition);
    }
}
