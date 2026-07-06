package com.ly.ttd.feature.flink.task.watermark;

import com.ly.ttd.feature.flink.config.AppConfig;
import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkOutput;

/**
 * @author yong.li
 * @since 2026/6/16 12:47
 */
public class DynamicWatermarkGenerator<T> implements WatermarkGenerator<T> {
    private long maxTimestamp = 0L;
    // 关键：延迟值不从构造函数传入，而是从一个外部服务获取
    private AppConfig appConfig;

    public DynamicWatermarkGenerator(AppConfig appConfig) {
        this.appConfig = appConfig;
        maxTimestamp = Long.MIN_VALUE + appConfig.getDelayTime() + 1L;
    }

    @Override
    public void onEvent(T o, long eventTimestamp, WatermarkOutput watermarkOutput) {
        this.maxTimestamp = Math.max(this.maxTimestamp, eventTimestamp);
    }

    @Override
    public void onPeriodicEmit(WatermarkOutput watermarkOutput) {
        watermarkOutput.emitWatermark(new Watermark(this.maxTimestamp - appConfig.getDelayTime() - 1L));
    }

}
