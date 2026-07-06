package com.ly.ttd.feature.sample.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ttd_data_struct")
public class DataStructEntity extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String resourceKey;
    private String resourceName;
    private String version;
}
