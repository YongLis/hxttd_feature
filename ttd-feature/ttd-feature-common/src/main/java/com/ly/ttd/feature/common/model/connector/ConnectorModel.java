package com.ly.ttd.feature.common.model.connector;

import com.ly.ttd.feature.common.model.BaseResourceModel;
import com.ly.ttd.feature.common.model.DataFieldModel;
import lombok.Data;

import java.util.List;

/**
 * @author yong.li
 * @since 2026/6/12 16:17
 */
@Data
public class ConnectorModel extends BaseResourceModel {

    private String connectorType;
    /**
     * 前置条件
     */
    private String condition;
    /**
     * 参数列表
     */
    private List<DataFieldModel> fields;


}
