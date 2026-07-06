package com.ly.ttd.feature.response;


import java.util.Date;

/**
 * 实时特征分组
 */

public class VelocityPointMappingModel {

    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 接入点
     */
    private String pointCode;

    /**
     * 实时特征代码
     */
    private String velocityCode;

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

    public Long getProjectId() {
        return projectId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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