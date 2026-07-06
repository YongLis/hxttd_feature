package com.ly.ttd.feature.response.redis;


import com.ly.ttd.feature.common.enums.ObjectTypeEnum;
import com.ly.ttd.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 单维度实时特征历史数据
 *
 * @author yong.li
 * @since 2026/3/18 17:01
 */

public class SingleDimsVelHistoryData {
    /**
     * 缓存key =实时特征代码主维度值
     */
    private String redisKey;
    /**
     * 值类型：Date时间、 BigDecimal金额、Long 整数，String字符串
     */
    private String valueType;

    /**
     * 当前值
     */
    private String value;
    /**
     * 过期时间
     */
    private Date expireTime;

    public SingleDimsVelHistoryData(String redisKey, String valueType, String value, Date expireTime) {
        this.redisKey = redisKey;
        this.valueType = valueType;
        this.value = value;
        this.expireTime = expireTime;
    }

    /**
     * 获取字符串
     */
    public String getString() {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return ObjectTypeEnum.STRING.getCode().equals(valueType) ? value : null;
    }

    /**
     * 获取金额
     */
    public BigDecimal getDecimal() {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return ObjectTypeEnum.DOUBLE.getCode().equals(valueType) ? new BigDecimal(value) : null;
    }

    /**
     * 获取整型
     */
    public Integer getInt() {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return ObjectTypeEnum.LONG.getCode().equals(valueType) ? Integer.valueOf(value) : null;
    }

    /**
     * 获取日期
     */
    public Date getDate() {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return ObjectTypeEnum.DATE.getCode().equals(valueType) ? DateUtil.parse("yyyy-MM-dd HH:mm:ss", value) : null;
    }


    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
