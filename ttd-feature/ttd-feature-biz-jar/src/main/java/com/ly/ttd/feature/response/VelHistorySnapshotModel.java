package com.ly.ttd.feature.response;

import com.ly.ttd.utils.Md5Util;

import java.util.Date;

/**
 * 实时特征历史快照模型
 *
 * @author yong.li
 * @since 2026/3/18 16:27
 */
public class VelHistorySnapshotModel {

    /**
     * 行记录key=分类编码+实时特征代码 +主维度值+交易号
     */
    private String rowkey;
    /**
     * 分类编码
     */
    private String classificationCode;
    /**
     * 实时特征代码
     */
    private String velocityCode;
    /**
     * 主维度值
     */
    private String masterValue;
    /**
     * 交易号
     */
    private String txnId;
    /**
     * 变更前
     */
    private String beforeData;
    /**
     * 变更后
     */
    private String afterData;
    /**
     * 交易时间
     */
    private Date txnTime;
    /**
     * 过期时间
     */
    private Date expireTime;


    public String getRowkey() {
        return Md5Util.MD5(this.classificationCode + "_" + this.velocityCode + "_" + this.masterValue + "_" + this.txnId);
    }

    public String getClassificationCode() {
        return classificationCode;
    }

    public void setClassificationCode(String classificationCode) {
        this.classificationCode = classificationCode;
    }

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

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getBeforeData() {
        return beforeData;
    }

    public void setBeforeData(String beforeData) {
        this.beforeData = beforeData;
    }

    public String getAfterData() {
        return afterData;
    }

    public void setAfterData(String afterData) {
        this.afterData = afterData;
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
