package com.ly.ttd.feature.srv.vel.compile;

import com.ly.ttd.feature.common.event.doris.VelEventData;
import com.ly.ttd.feature.common.model.vel.FeatureConfigModel;
import com.ly.ttd.feature.srv.vel.compile.jexl3.LocalMapContext;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/4/29 19:32
 */
@Data
public class FeatureConfigExecuteContext {
    private String endpointCode;
    private String txnId;
    private String memberId;
    private Date txnTime;
    private FeatureConfigModel configModel;

    private LocalMapContext context;

    private Map<String, Object> params;

    private List<VelEventData> eventDataDos;

    //read||write
    private String option = "write";


}

