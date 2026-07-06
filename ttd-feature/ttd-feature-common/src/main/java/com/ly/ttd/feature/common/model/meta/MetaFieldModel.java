package com.ly.ttd.feature.common.model.meta;

import com.ly.ttd.feature.common.model.BaseResourceModel;
import lombok.Data;

/**
 * 元字段
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class MetaFieldModel extends BaseResourceModel {
    /**
     * 脚本语言(aviator/groovy)
     */
    private String language;

    /**
     * 计算脚本
     */
    private String script;

    /**
     * 接入点
     */
    private String pointCode;

}
