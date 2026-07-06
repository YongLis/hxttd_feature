package com.ly.ttd.feature.srv.vel.compile;

import lombok.Data;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlExpression;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 实时特征配置表达式
 *
 * @author yong.li
 * @since 2026/4/18 16:52
 */
@Data
public class FeatureConfigExpress {

    /**
     * 条件表达式（编译后）
     */
    private JexlExpression conditionExpress;

    /**
     * 主维度表达式（编译后）
     */
    private JexlExpression masterExpress;

    /**
     * 从维度表达式（编译后）
     */
    private JexlExpression slaveExpress;

    /**
     * 实时特征值表达式（编译后）
     */
    private JexlExpression valueExpress;

    public FeatureConfigExpress(JexlExpression conditionExpress, JexlExpression masterExpress,
                                JexlExpression slaveExpress, JexlExpression valueExpress) {
        this.conditionExpress = conditionExpress;
        this.masterExpress = masterExpress;
        this.slaveExpress = slaveExpress;
        this.valueExpress = valueExpress;
    }

    /**
     * 获取筛选条件的执行结果
     */
    public Boolean execConditionExpress(JexlContext jexlContext) {
        return (Boolean) conditionExpress.evaluate(jexlContext);
    }

    /**
     * 获取主维度执行结果
     * 主维度结果存在2种情况：
     * 1、维度由多个字段组成，如何会员号,设备号， 此时输出的主维度key为：会员号+设备号
     * 2、维度只有一个字段，但该字段可能是一个集合，此时输出的主维度key为：集合中所有的key; 如：[1,2,3] 输出的key为：1,2,3; 字段是单独的一个值，输出的key为：字段值
     * 故主维度结果为List<String>类型
     */
    public List<String> execMasterExpress(JexlContext jexlContext) {
        Object obj = masterExpress.evaluate(jexlContext);

        if (obj instanceof String) {
            return Arrays.asList(obj.toString()
                    .split(","));
        } else if (obj instanceof Collection) {
            return ((Collection<?>) obj).stream().map(String::valueOf).collect(Collectors.toList());
        }
        return (List<String>) masterExpress.evaluate(jexlContext);
    }

    /**
     * 获取从维度执行结果
     */
    public List<String> execSlaveExpress(JexlContext jexlContext) {
        Object obj = slaveExpress.evaluate(jexlContext);
        if (obj instanceof String) {
            return Arrays.asList(obj.toString()
                    .split(","));
        } else if (obj instanceof Collection) {
            return ((Collection<?>) obj).stream().map(String::valueOf).collect(Collectors.toList());
        }
        return (List<String>) slaveExpress.evaluate(jexlContext);
    }

    /**
     * 获取实时特征值执行结果
     */
    public BigDecimal execValueExpress(JexlContext jexlContext) {
        return new BigDecimal(valueExpress.evaluate(jexlContext)
                .toString());
    }

}
