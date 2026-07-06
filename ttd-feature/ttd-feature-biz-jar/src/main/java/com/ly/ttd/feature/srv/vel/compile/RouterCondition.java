package com.ly.ttd.feature.srv.vel.compile;

import lombok.Data;

/**
 * 一级条件
 *
 * @author yong.li
 * @since 2026/4/3 09:35
 */
@Data
public class RouterCondition {

    /**
     * 字段
     */
    private DataFrame field;
    /**
     * 操作符
     */
    private String op;
    /**
     * 悬着框值对象
     */
    private DataFrame value;

    /**
     * 值类型:
     * input 输入
     * select 选择
     */
    private String valueType;

    /**
     * 输入框值
     */
    private String fixValue;


    public RouterCondition() {
    }
}
