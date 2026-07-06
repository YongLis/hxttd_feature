package com.ly.ttd.feature.common.model.factor.resource;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 元字段资源
 *
 * @author yong.li
 * @since 2026/6/9 10:55
 */
@Data
public class MetaFactorResourceModel {

    private String metaFieldCode;


    public static MetaFactorResourceModel convertFromJson(String json) {
        if (StringUtils.isNoneEmpty(json)) {
            return JSON.parseObject(json, MetaFactorResourceModel.class);
        }
        return null;
    }

}
