package com.ly.ttd.biz.feature.dem.sweb.service.sequence.impl;

import com.ly.ttd.biz.feature.dem.sweb.consts.SequenceConst;
import com.ly.ttd.biz.feature.dem.sweb.service.sequence.SequenceQueryService;
import com.ly.ttd.feature.admin.api.sequence.SequenceService;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import com.ly.ttd.inf.rpc.api.annotation.Rpcwired;
import org.springframework.stereotype.Service;

/**
 * 序列查询服务实现
 *
 * @author yong.li
 * @since 2026-06-23
 */
@Service
public class SequenceQueryServiceImpl implements SequenceQueryService {
    @Rpcwired
    private SequenceService sequenceService;

    @Override
    public String getNextPointCode() {
        return sequenceService.generateSeq(FeatureResourceType.POINT.getPrefix(), 5, SequenceConst.END_POINT);
    }
}
