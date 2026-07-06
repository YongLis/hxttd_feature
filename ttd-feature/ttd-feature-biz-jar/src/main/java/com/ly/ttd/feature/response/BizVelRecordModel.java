package com.ly.ttd.feature.response;

import java.util.Date;
import java.util.Map;

/**
 * 实时特征累计入参日志
 *
 * @author yong.li
 * @since 2026/3/18 16:14
 */

public class BizVelRecordModel {
    /**
     * 交易号
     */
    private String txnId;
    /**
     * 支付方式
     */
    private String payType;
    /**
     * 业务代码
     */
    private String bizCode;
    /**
     * 支付方式业务代码交易号
     */
    private String rowKey;
    /**
     * 参数集
     */
    private Map<String, Object> dataMap;
    /**
     * 交易时间
     */
    private Date txnTime;
    /**
     * 过期时间（默认保留60天日志数据）
     */
    private Date expireTime;

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    public Date getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(Date txnTime) {
        this.txnTime = txnTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
