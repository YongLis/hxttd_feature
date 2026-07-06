package com.ly.ttd.feature.srv.data;

/**
 * 轨迹数据保存服务（kafka转存Doris）
 *
 * @author yong.li
 * @since 2026/6/15 14:33
 */
public interface TraceDataSaveService {


    /**
     * 保存轨迹数据
     */
    void save(TraceMessage traceMessage, String runMode);
}
