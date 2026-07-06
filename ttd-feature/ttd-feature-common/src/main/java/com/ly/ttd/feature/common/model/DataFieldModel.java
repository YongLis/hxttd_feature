package com.ly.ttd.feature.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author yong.li
 * @since 2026/5/24 06:27
 */
@Data
@AllArgsConstructor
public class DataFieldModel {

    private String fieldCode;

    private String fieldName;

    private String fieldType;
}
