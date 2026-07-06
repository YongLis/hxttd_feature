package com.ly.ttd.feature.srv;


import com.ly.ttd.feature.request.TxnFeatureRequest;

/**
 * 实时特征累计服务
 *
 * @author yong.li
 * @since 2026/3/19 16:20
 */
public interface TxnFeatureService {
    /**
     * 实时特征累计(异步)
     */
    void write(TxnFeatureRequest request);
}
