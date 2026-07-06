package com.ly.ttd.feature.srv.vel.compile;

import lombok.Data;

/**
 * 简单条件
 *
 * @author yong.li
 * @since 2026/4/3 09:35
 */
@Data
public class SimpleCondition {

    /**
     * 字段
     */
    private DataFrame field;
    /**
     * 操作符
     */
    private String op;
    /**
     * 值
     */
    private DataFrame value;

    /**
     * 值类型:
     * string,字符串类型
     * integer 整数类型
     * float浮点类型
     * date日期类型
     * list列表类型
     */
    private String valueType;

    /**
     * 常量值
     */
    private String fixValue;
}
