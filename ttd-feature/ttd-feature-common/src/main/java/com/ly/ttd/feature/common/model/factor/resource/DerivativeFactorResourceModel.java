package com.ly.ttd.feature.common.model.factor.resource;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.feature.common.model.factor.ConnectorParamField;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author yong.li
 * @since 2026/6/9 10:57
 */
@Data
public class DerivativeFactorResourceModel {

    /**
     * 关联指标编码
     */
    private List<String> factorCodes;

    /**
     * 连接类型
     */
    private String connectorType;

    /**
     * 连接编码
     */
    private String connectorCode;

    /**
     * 连接参数
     */
    private List<ConnectorParamField> params;
    /**
     * 脚本语言(aviator/groovy)
     */
    private String language;

    /**
     * 前置脚本
     */
    private String conditionScript;


    /**
     * 计算脚本
     */
    private String script;

    public static DerivativeFactorResourceModel convertFromJson(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return JSON.parseObject(json, DerivativeFactorResourceModel.class);
    }


}
