package com.ly.ttd.feature.srv.vel;

import com.ly.ttd.consts.exception.BizException;
import com.ly.ttd.feature.request.TxnFeatureRequest;
import com.ly.ttd.feature.srv.TxnFeatureService;
import com.ly.ttd.feature.srv.vel.sunk.FeatureDataSunkTask;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 实时特征服务
 *
 * @author yong.li
 * @since 2026/4/25 14:01
 */
@Service
@Slf4j
public class TxnFeatureServiceImpl implements TxnFeatureService {

    @Resource
    private FeatureDataSunkTask featureDataSunkTask;

    @Override
    public void write(TxnFeatureRequest request) {
        try {
            featureDataSunkTask.execute(request);
        } catch (BizException e) {
            log.error("writeFeature error, txnId={}", request.getTxnId(), e);
        }

    }
}
