package com.ly.ttd.connector.jdbc;

import com.ly.ttd.feature.common.enums.ObjectTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author yong.li
 * @since 2026/4/14 11:06
 */
@Slf4j
public class JdbcExecutor {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcExecutor(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public Object execute(String sql, Map<String, Object> param, String returnType) throws Exception {
        ObjectTypeEnum typeEnum = ObjectTypeEnum.getByCode(returnType);
        if (typeEnum == null) {
            throw new IllegalArgumentException("不支持的返回类型: " + returnType);
        }
        switch (typeEnum) {
            case DICT:
            case LONG:
            case DATE:
            case DOUBLE:
            case STRING:
            case BOOLEAN:
            case DECIMAL:
                return queryForMap(sql, param);
            case LIST:
                return queryForList(sql, param);
            default:
                throw new IllegalArgumentException("不支持的返回类型: " + returnType);
        }
    }

    /**
     * 执行查询(单条记录)
     */
    private Map<String, Object> queryForMap(String sql, Map<String, Object> param) throws Exception {
        return jdbcTemplate.queryForMap(sql, param);
    }

    private List<Map<String, Object>> queryForList(String sql, Map<String, Object> param) throws Exception {
        return jdbcTemplate.queryForList(sql, param);
    }

}
