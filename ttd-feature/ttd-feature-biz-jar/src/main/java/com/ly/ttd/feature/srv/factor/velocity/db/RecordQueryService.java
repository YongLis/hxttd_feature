package com.ly.ttd.feature.srv.factor.velocity.db;

import com.ly.ttd.feature.common.event.dto.VelValueItem;

import java.util.List;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/6/11 10:50
 */
public interface RecordQueryService {

    /**
     * 查询记录
     */
    Map<String, List<VelValueItem>> queryData(String txnId, String uniqueKey);

}
