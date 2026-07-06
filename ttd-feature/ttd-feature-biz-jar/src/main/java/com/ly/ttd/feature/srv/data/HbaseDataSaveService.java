package com.ly.ttd.feature.srv.data;

import com.ly.ttd.feature.common.event.hbase.HbaseData;

import java.io.IOException;
import java.util.List;

/**
 * Hbase数据保存服务
 *
 * @author yong.li
 * @since 2026/7/4 21:54
 */
public interface HbaseDataSaveService {

    /**
     * 保存Hbase数据
     */
    void write(String table, HbaseData hbaseData) throws IOException;

    /**
     * 批量保存Hbase数据
     */
    void batchWrite(String table, List<HbaseData> hbaseDataList) throws IOException;
}
