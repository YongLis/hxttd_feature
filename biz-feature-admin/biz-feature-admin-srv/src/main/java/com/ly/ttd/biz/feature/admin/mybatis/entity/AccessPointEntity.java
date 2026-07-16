package com.ly.ttd.biz.feature.admin.mybatis.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ly.ttd.feature.admin.api.dto.AccessPointParamDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 接入点实体 (表: ttd_access_point)
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ttd_access_point")
public class AccessPointEntity extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String code;

    private String name;

    private String version;

    private String remark;

    private Long projectId;

    private String apiJson;

    public List<AccessPointParamDto> apiConvert() {
        return JSON.parseArray(apiJson, AccessPointParamDto.class);
    }
}
