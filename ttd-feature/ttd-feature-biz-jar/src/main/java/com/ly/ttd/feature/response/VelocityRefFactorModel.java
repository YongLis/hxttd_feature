package com.ly.ttd.feature.response;

import java.util.Date;

/**
 * 实时特征配置依赖因子
 *
 * @author yong.li
 * @since 2026/3/18 15:36
 */
public class VelocityRefFactorModel {
    private Long id;
    /**
     * 接入点编码
     */
    private String pointCode;

    /**
     * 实时特征编码
     */
    private String velocityCode;
    /**
     * 数据集名称
     */
    private String dataMapName;

    /**
     * 数据集key
     */
    private String mapKey;
    /**
     * 删除标志
     */
    private Boolean deleted;

    /**
     * 操作者
     */
    private String operator;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date crtTime;

    /**
     * 更新时间
     */
    private Date uptTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPointCode() {
        return pointCode;
    }

    public void setPointCode(String pointCode) {
        this.pointCode = pointCode;
    }

    public String getVelocityCode() {
        return velocityCode;
    }

    public void setVelocityCode(String velocityCode) {
        this.velocityCode = velocityCode;
    }

    public String getDataMapName() {
        return dataMapName;
    }

    public void setDataMapName(String dataMapName) {
        this.dataMapName = dataMapName;
    }

    public String getMapKey() {
        return mapKey;
    }

    public void setMapKey(String mapKey) {
        this.mapKey = mapKey;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    public Date getUptTime() {
        return uptTime;
    }

    public void setUptTime(Date uptTime) {
        this.uptTime = uptTime;
    }
}
