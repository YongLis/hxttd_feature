package com.ly.ttd.feature.srv.vel.monitor;

import com.ly.ttd.feature.common.event.doris.VelEventData;
import com.ly.ttd.feature.common.event.doris.VelHistorySnapshot;
import com.ly.ttd.feature.common.event.doris.VelMasterValueRecord;
import com.ly.ttd.feature.common.event.doris.VelReadSnapshot;

import java.util.List;

/**
 * 实时特征监控数据落库服务
 *
 * @author yong.li
 * @since 2026/4/25 15:42
 */

public interface FeatureMonitorSinkService {

    /**
     * 异步保存实时特征事件流数据
     *
     * @param dos     实时特征事件流数据
     * @param runMode 运行模式
     */
    void syncSaveVelEventData(List<VelEventData> dos, String runMode);

    /**
     * 异步保存实时特征主维度值记录
     *
     * @param record  实时特征主维度值记录
     * @param runMode 运行模式
     */
    void syncSaveVelMasterValueRecord(VelMasterValueRecord record, String runMode);

    /**
     * 异步保存实时特征累计快照数据
     *
     * @param dto     实时特征累计快照数据
     * @param runMode 运行模式
     */
    void syncSaveVelHistorySnapshot(VelHistorySnapshot dto, String runMode);

    /**
     * 异步保存实时特征查询快照数据
     *
     * @param runMode 运行模式
     */
    void syncSaveVelReadSnapshot(VelReadSnapshot dto, String runMode);
}
