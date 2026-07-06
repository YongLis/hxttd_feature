package com.ly.ttd.feature.sample.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ttd_data_field")
public class DataFieldEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String dataStructCode;
    private String fieldCode;
    private String fieldName;
    private String factorCode;
    private String objectType;
    private String defaultValue;
    private Integer sortOrder;
}
