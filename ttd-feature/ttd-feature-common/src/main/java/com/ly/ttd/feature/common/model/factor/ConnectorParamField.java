package com.ly.ttd.feature.common.model.factor;

import lombok.Data;

/**
 * @author yong.li
 * @since 2026/7/2 09:38
 */
@Data
public class ConnectorParamField {

    private String fieldCode;

    private String sourceCode;

    public ConnectorParamField(String fieldCode, String sourceCode) {
        this.fieldCode = fieldCode;
        this.sourceCode = sourceCode;
    }
}
