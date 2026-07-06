package com.ly.ttd.feature.srv.vel.sunk;

import com.ly.ttd.consts.exception.BizException;
import com.ly.ttd.feature.request.TxnFeatureRequest;
import com.ly.ttd.feature.srv.vel.monitor.FeatureMonitorSinkService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author yong.li
 * @since 2026/4/23 15:04
 */
@Component
public abstract class FeatureTask {

    @Resource
    protected FeatureMonitorSinkService featureMonitorSinkService;

    /**
     * 执行
     *
     * @param request 上下文
     * @throws BizException
     */
    public abstract void execute(TxnFeatureRequest request) throws BizException;

}
