package com.ly.ttd.feature.response;

import java.util.Date;

/**
 * 实时特征记录模型
 *
 * @author yong.li
 * @since 2026/3/18 16:19
 */

public class VelRecordModel {

    /**
     * 实时特征编码
     */
    private String velocityCode;
    /**
     * 主维度值
     */
    private String masterValue;
    /**
     * 缓存key=实时特征编码+主维度值
     */
    private String redisKey;
    /**
     * 缓存item长度
     */
    private Integer cacheSize;
    /**
     * 过期时间
     */
    private Date expireTime;

    public String getVelocityCode() {
        return velocityCode;
    }

    public void setVelocityCode(String velocityCode) {
        this.velocityCode = velocityCode;
    }

    public String getMasterValue() {
        return masterValue;
    }

    public void setMasterValue(String masterValue) {
        this.masterValue = masterValue;
    }

    public String getRedisKey() {
        return this.velocityCode + "_" + this.masterValue;
    }

    public Integer getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(Integer cacheSize) {
        this.cacheSize = cacheSize;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
