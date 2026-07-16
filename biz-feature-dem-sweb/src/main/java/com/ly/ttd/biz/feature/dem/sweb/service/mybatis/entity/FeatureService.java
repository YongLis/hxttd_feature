package com.ly.ttd.biz.feature.dem.sweb.service.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 特征服务表
 *
 * @TableName ttd_feature_service
 */
@Data
@TableName("ttd_feature_service")
public class FeatureService extends BaseEntity {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long serviceId;

    /**
     * 服务名称
     */
    @TableField("service_name")
    private String serviceName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 特征name列表，多个按逗号隔开
     */
    @TableField("feature_list")
    private String featureList;
}