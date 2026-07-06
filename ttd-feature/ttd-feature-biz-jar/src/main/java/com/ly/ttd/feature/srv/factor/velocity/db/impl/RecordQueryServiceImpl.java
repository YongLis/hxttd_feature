package com.ly.ttd.feature.srv.factor.velocity.db.impl;

import com.ly.ttd.feature.common.event.dto.VelValueItem;
import com.ly.ttd.feature.srv.factor.velocity.db.RecordQueryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/6/11 10:51
 */
@Service
public class RecordQueryServiceImpl implements RecordQueryService {


    @Override
    public Map<String, List<VelValueItem>> queryData(String txnId, String uniqueKey) {
        return null;
    }
}
