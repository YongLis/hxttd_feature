package com.ly.ttd.feature.srv.vel.compile;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 实时特征配置
 */
@Data
public class FeatureConfig {
    /**
     * 实时特征编码
     */
    private String velocityCode;

    /**
     * 前置条件
     */
    private String conditionScript;

    /**
     * 主维度
     */
    private String masterKey;

    /**
     * 主维度维度列个数
     */
    private Integer masterColumnNum;

    /**
     * 从维度
     */
    private String slaveKey;
    /**
     * 实时特征值
     */
    private String velValue;
    /**
     * 实时特征值类型
     */
    private String valueType;

    /**
     * 累计方式：ttl按过期时间，cd自然日，cm自然月，cy自然年，fvr永久累计
     */
    private String cumType;

    /**
     * 计算方式： SUM求和，COUNT计数，AVG平均值， MIN最小值，MAX最大值
     */
    private String calateType;

    /**
     * 过期时间数
     */
    private Integer expireNum;

    /**
     * 过期时间单位（年月日时分）
     */
    private String expireType;

    /**
     * 关联元字段
     */
    private Set<String> refMetaFields;

    /**
     * 编译后的表达式
     */
    private FeatureConfigExpress express;

    private List<String> pointCodes;

    private String definitionKey;
}