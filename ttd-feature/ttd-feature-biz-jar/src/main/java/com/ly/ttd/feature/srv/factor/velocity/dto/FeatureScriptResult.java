package com.ly.ttd.feature.srv.factor.velocity.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yong.li
 * @since 2026/6/11 09:58
 */
@Data
public class FeatureScriptResult {

    private Boolean condition;

    private List<String> master;

    private List<String> slave;

    private BigDecimal value;

}
