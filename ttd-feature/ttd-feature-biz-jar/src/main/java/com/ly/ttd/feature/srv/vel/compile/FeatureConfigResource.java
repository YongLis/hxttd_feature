package com.ly.ttd.feature.srv.vel.compile;

import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 实时特征配置资源
 *
 * @author yong.li
 * @since 2026/4/8 13:53
 */
@Data
public class FeatureConfigResource {

    /**
     * 主键
     */
    private Long id;

    /**
     * 接入点元字段编码
     */
    private String endpointMetaCode;
    /**
     * 网关订单号元字段编码
     */
    private String gatewayOrderIdMetaCode;


    /**
     * 实时特征编码
     */
    private String velocityCode;

    /**
     * 统计主体
     */

    private List<DataFrame> master;

    /**
     * 取值逻辑
     */
    private DataFrame value;

    /**
     * 取值类型
     */
    private String valueType;
    /**
     * 当前值
     */
    private BigDecimal current;
    /**
     * 筛选条件
     */
    private List<FeatureCondition> condition;

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

    private String definitionKey;


    public FeatureConfigResource() {
    }

    public String buildCompareString() {
        if (CollectionUtils.isNotEmpty(master) || null == value) {
            return "";
        }
        return String.format("%s_%s_%s_%s_%s_%s_%s", StringUtils.join(master.stream().map(DataFrame::getColumnName).collect(Collectors.toList()), "#"),
                cumType, calateType, expireNum, expireType, valueType, value.getColumnName());
    }
}
