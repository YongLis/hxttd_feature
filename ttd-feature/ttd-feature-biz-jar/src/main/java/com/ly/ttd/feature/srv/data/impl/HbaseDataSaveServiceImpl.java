package com.ly.ttd.feature.srv.data.impl;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.cfg.FeatureConfigurationAware;
import com.ly.ttd.feature.cfg.ThreadPoolNames;
import com.ly.ttd.feature.common.event.hbase.HbaseData;
import com.ly.ttd.feature.srv.data.HbaseDataSaveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yong.li
 * @since 2026/7/4 23:11
 */
@Service
@Slf4j
public class HbaseDataSaveServiceImpl implements HbaseDataSaveService, FeatureConfigurationAware {

    private FeatureConfiguration featureConfiguration;

    /**
     * 单行写入
     */
    @Override
    public void write(String table, HbaseData hbaseData) throws IOException {
        ThreadPoolExecutor executor = featureConfiguration.getThreadPool(ThreadPoolNames.THREAD_HBASE_CLIENT);
        TableName tableName = TableName.valueOf(table);
        Connection connection = featureConfiguration.getConnectors().getHbaseClient();
        try (Table tab = connection.getTable(tableName, executor)) {
            Put put = new Put(hbaseData.getRowKey().getBytes(StandardCharsets.UTF_8));
            put.addColumn(Bytes.toBytes(hbaseData.getFamily()),
                    Bytes.toBytes(hbaseData.getQualifier()),
                    hbaseData.getValue().getBytes(StandardCharsets.UTF_8));
            tab.put(put);
        } catch (Exception e) {
            log.error("hbase write error, table={}, rowKey={}, value={}", table, hbaseData.getRowKey(), hbaseData.getValue());
            throw e;
        }
    }

    /**
     * 批量写入
     */
    @Override
    public void batchWrite(String table, List<HbaseData> hbaseDataList) throws IOException {
        ThreadPoolExecutor executor = featureConfiguration.getThreadPool(ThreadPoolNames.THREAD_HBASE_CLIENT);

        TableName tableName = TableName.valueOf(table);
        Connection connection = featureConfiguration.getConnectors().getHbaseClient();
        try (Table tab = connection.getTable(tableName, executor)) {
            List<Put> puts = new ArrayList<>();
            for (HbaseData hbaseData : hbaseDataList) {
                Put put = new Put(hbaseData.getRowKey().getBytes(StandardCharsets.UTF_8));
                put.addColumn(Bytes.toBytes(hbaseData.getFamily()),
                        Bytes.toBytes(hbaseData.getQualifier()),
                        hbaseData.getValue().getBytes(StandardCharsets.UTF_8));
                puts.add(put);
            }
            tab.put(puts);
        } catch (Exception e) {
            log.error("hbase batch write error, table={}, hbaseDataList={}", table, JSON.toJSONString(hbaseDataList));
            throw e;
        }
    }

    @Override
    public void setFeatureConfiguration(FeatureConfiguration featureConfiguration) {
        this.featureConfiguration = featureConfiguration;
    }
}
