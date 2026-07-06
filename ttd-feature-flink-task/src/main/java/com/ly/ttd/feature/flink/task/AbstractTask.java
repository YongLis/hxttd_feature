package com.ly.ttd.feature.flink.task;

/**
 * @author yong.li
 * @since 2026/5/30 11:57
 */

import com.ly.ttd.feature.common.enums.VelocityCaluateTypeEnum;
import com.ly.ttd.feature.flink.config.AppConfig;
import jakarta.annotation.Resource;


public abstract class AbstractTask {
    @Resource
    protected AppConfig appConfig;

    /**
     * 获取计算类型
     */
    public abstract VelocityCaluateTypeEnum getCaluateTypeEnum();


    public abstract void start() throws Exception;


}
