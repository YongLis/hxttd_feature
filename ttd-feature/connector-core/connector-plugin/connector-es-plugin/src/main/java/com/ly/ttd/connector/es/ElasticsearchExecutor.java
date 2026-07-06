package com.ly.ttd.connector.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.WrapperQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/4/14 11:06
 */
public class ElasticsearchExecutor {

    private ElasticsearchClient client;

    public ElasticsearchExecutor(ElasticsearchClient client) {
        this.client = client;
    }

    /**
     * 执行查询
     */
    public Map<String, Object> execute(String endpoint, String dsl) throws IOException {
        Query query = Query.of(t ->
                t.wrapper(WrapperQuery.of(f -> f.query(dsl)))
        );

        SearchResponse<Map> mapSearchResponse = client.search(t ->
                t.index(endpoint)
                        .query(query), Map.class);
        return JSON.parseObject(JSON.toJSONString(mapSearchResponse), Map.class);
    }

}
