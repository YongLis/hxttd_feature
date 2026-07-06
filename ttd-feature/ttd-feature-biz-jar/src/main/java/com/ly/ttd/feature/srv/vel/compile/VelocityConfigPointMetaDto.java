package com.ly.ttd.feature.srv.vel.compile;

import lombok.Data;

import java.util.List;

/**
 * 实时特征配置
 */
@Data
public class VelocityConfigPointMetaDto {
    private Long id;

    /**
     * 实时特征编码
     */
    private String velocityCode;

    /**
     * 配置文件
     */
    private String file;

    private String remark;
    /**
     * 前置条件：格式[{"pointCode": "XXXX", "condition":"XXXX"}]
     */
    private String conditionScript;
    /**
     * 主维度
     */
    private String masterKey;
    /**
     * 从维度
     */
    private String slaveKey;
    /**
     * 实时特征值
     */
    private String velValue;
    /**
     * 实时特征值类型：input固定值，select变量值
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


    private List<String> pointCodes;
    private List<String> refMetas;
}