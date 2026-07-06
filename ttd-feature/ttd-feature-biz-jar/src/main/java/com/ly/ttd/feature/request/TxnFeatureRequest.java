package com.ly.ttd.feature.request;


import com.ly.ttd.feature.common.event.doris.VelEventData;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 实时特征累计入参
 *
 * @author yong.li
 * @since 2026/3/19 16:22
 */
@Data
public class TxnFeatureRequest {

    @NotBlank
    private String pointCode;
    @NotBlank
    private String txnId;
    @NotBlank
    private String bizCode;

    private Date txnTime;

    private String runMode;

    private List<VelEventData> eventDataList;

}
