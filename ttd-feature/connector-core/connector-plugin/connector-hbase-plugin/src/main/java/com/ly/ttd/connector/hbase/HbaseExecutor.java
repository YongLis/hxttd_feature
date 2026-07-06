package com.ly.ttd.connector.hbase;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/4/14 11:06
 */
@Slf4j
public class HbaseExecutor {

    private Connection connection;

    public HbaseExecutor(Connection connection) {
        this.connection = connection;
    }

    /**
     * 执行查询
     */
    public Map<String, Object> query(String table, String rowKey, String family, String qualifier) throws IOException {
        byte[] res = connection.getTable(TableName.valueOf(table))
                .get(new Get(rowKey.getBytes(StandardCharsets.UTF_8)))
                .getValue(Bytes.toBytes(family),
                        Bytes.toBytes(qualifier));
        return JSON.parseObject(Bytes.toString(res), Map.class);
    }


}
