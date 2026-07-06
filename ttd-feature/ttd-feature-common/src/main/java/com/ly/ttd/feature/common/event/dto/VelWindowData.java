package com.ly.ttd.feature.common.event.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 特征变更窗口数据
 *
 * @author yong.li
 * @since 2026/5/20 16:44
 */
@Data
public class VelWindowData {
    /**
     * redis缓存key
     */
    private String redisKey;
    /**
     * 速率编码
     */
    private String featureCode;
    /**
     * 主值
     */
    private String masterValue;
    /**
     * 交易号
     */
    private String txnId;

    /**
     * 快照数据(变更前)
     */
    private Map<String, List<VelValueItem>> beforeData;
    /**
     * 快照数据(变更后)
     */
    private Map<String, List<VelValueItem>> afterData;

    /**
     * 过期时间
     */
    private Date expireTime;
}
