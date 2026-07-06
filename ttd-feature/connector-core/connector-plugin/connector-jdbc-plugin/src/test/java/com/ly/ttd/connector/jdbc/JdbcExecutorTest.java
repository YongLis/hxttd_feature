package com.ly.ttd.connector.jdbc;

import junit.framework.TestCase;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JdbcExecutor 单元测试
 * 基于 H2 内存数据库，测试表: t_order
 *
 * @author yong.li
 */
public class JdbcExecutorTest extends TestCase {

    private JdbcExecutor executor;

    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS t_order (" +
                    "    id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                    "    order_id VARCHAR(64) NOT NULL," +
                    "    member_id VARCHAR(64) NOT NULL," +
                    "    merchant_id VARCHAR(64) NOT NULL," +
                    "    order_amt DECIMAL(12,2) NOT NULL," +
                    "    crt_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "    order_status INT NOT NULL DEFAULT 0" +
                    ")";

    private static final String INSERT_DATA_SQL =
            "INSERT INTO t_order (order_id, member_id, merchant_id, order_amt, crt_time, order_status) " +
                    "VALUES (:orderId, :memberId, :merchantId, :orderAmt, :crtTime, :orderStatus)";

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // 初始化 H2 内存数据库
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        executor = new JdbcExecutor(jdbcTemplate);

        // 建表
        jdbcTemplate.getJdbcTemplate().execute(CREATE_TABLE_SQL);

        // 清空数据
        jdbcTemplate.getJdbcTemplate().execute("DELETE FROM t_order");

        // 插入测试数据
        Map<String, Object> params1 = new HashMap<>();
        params1.put("orderId", "ORD20260612001");
        params1.put("memberId", "M10001");
        params1.put("merchantId", "MER2001");
        params1.put("orderAmt", 128.50);
        params1.put("crtTime", "2026-06-12 10:30:00");
        params1.put("orderStatus", 1);
        jdbcTemplate.update(INSERT_DATA_SQL, params1);

        Map<String, Object> params2 = new HashMap<>();
        params2.put("orderId", "ORD20260612002");
        params2.put("memberId", "M10002");
        params2.put("merchantId", "MER2001");
        params2.put("orderAmt", 256.80);
        params2.put("crtTime", "2026-06-12 11:00:00");
        params2.put("orderStatus", 2);
        jdbcTemplate.update(INSERT_DATA_SQL, params2);

        Map<String, Object> params3 = new HashMap<>();
        params3.put("orderId", "ORD20260612003");
        params3.put("memberId", "M10001");
        params3.put("merchantId", "MER2002");
        params3.put("orderAmt", 89.99);
        params3.put("crtTime", "2026-06-12 14:20:00");
        params3.put("orderStatus", 1);
        jdbcTemplate.update(INSERT_DATA_SQL, params3);
    }

    /**
     * 测试 LIST 返回类型 - 查询所有订单
     */
    public void testExecuteListReturnAll() throws Exception {
        String sql = "SELECT id, order_id, member_id, merchant_id, order_amt, crt_time, order_status FROM t_order ORDER BY id";
        Map<String, Object> param = new HashMap<>();

        Object result = executor.execute(sql, param, "LIST");

        assertNotNull("LIST 返回结果不应为 null", result);
        assertTrue("结果应为 List 类型", result instanceof List);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = (List<Map<String, Object>>) result;
        assertEquals("应有 3 条订单记录", 3, list.size());

        // 验证第一条记录
        Map<String, Object> firstRow = list.get(0);
        assertEquals("ORD20260612001", firstRow.get("ORDER_ID"));
        assertEquals("M10001", firstRow.get("MEMBER_ID"));
        assertEquals("MER2001", firstRow.get("MERCHANT_ID"));
    }

    /**
     * 测试 LIST 返回类型 - 带条件查询（按 member_id 筛选）
     */
    public void testExecuteListWithCondition() throws Exception {
        String sql = "SELECT order_id, order_amt, order_status FROM t_order WHERE member_id = :memberId ORDER BY order_id";
        Map<String, Object> param = new HashMap<>();
        param.put("memberId", "M10001");

        Object result = executor.execute(sql, param, "LIST");

        assertNotNull(result);
        assertTrue(result instanceof List);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = (List<Map<String, Object>>) result;
        assertEquals("会员 M10001 应有 2 条订单", 2, list.size());
    }

    /**
     * 测试 DICT 返回类型 - 查询单条订单记录
     */
    public void testExecuteDictReturnSingle() throws Exception {
        String sql = "SELECT order_id, member_id, merchant_id, order_amt, order_status " +
                "FROM t_order WHERE order_id = :orderId";
        Map<String, Object> param = new HashMap<>();
        param.put("orderId", "ORD20260612001");

        Object result = executor.execute(sql, param, "DICT");

        assertNotNull("DICT 返回结果不应为 null", result);
        assertTrue("结果应为 Map 类型", result instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, Object> row = (Map<String, Object>) result;
        assertEquals("ORD20260612001", row.get("ORDER_ID"));
        assertEquals("M10001", row.get("MEMBER_ID"));
        assertEquals("MER2001", row.get("MERCHANT_ID"));
    }

    /**
     * 测试 STRING 返回类型 - 查询单个字符串字段
     */
    public void testExecuteStringReturn() throws Exception {
        String sql = "SELECT order_id FROM t_order WHERE id = :id";
        Map<String, Object> param = new HashMap<>();
        param.put("id", 1L);

        Object result = executor.execute(sql, param, "STRING");

        assertNotNull(result);
        assertTrue("结果应为 Map 类型", result instanceof Map);
    }

    /**
     * 测试 LONG 返回类型 - 查询计数
     */
    public void testExecuteLongReturnCount() throws Exception {
        String sql = "SELECT COUNT(*) AS CNT FROM t_order WHERE merchant_id = :merchantId";
        Map<String, Object> param = new HashMap<>();
        param.put("merchantId", "MER2001");

        Object result = executor.execute(sql, param, "LONG");

        assertNotNull(result);
        assertTrue("结果应为 Map 类型", result instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, Object> row = (Map<String, Object>) result;
        assertEquals("商户 MER2001 应有 2 条订单", 2L, row.get("CNT"));
    }

    /**
     * 测试 DOUBLE 返回类型 - 查询金额汇总
     */
    public void testExecuteDoubleReturnSum() throws Exception {
        String sql = "SELECT SUM(order_amt) AS TOTAL_AMT FROM t_order WHERE member_id = :memberId";
        Map<String, Object> param = new HashMap<>();
        param.put("memberId", "M10001");

        Object result = executor.execute(sql, param, "DOUBLE");

        assertNotNull(result);
        assertTrue("结果应为 Map 类型", result instanceof Map);
    }

    /**
     * 测试 DECIMAL 返回类型 - 查询金额
     */
    public void testExecuteDecimalReturn() throws Exception {
        String sql = "SELECT order_amt FROM t_order WHERE order_id = :orderId";
        Map<String, Object> param = new HashMap<>();
        param.put("orderId", "ORD20260612001");

        Object result = executor.execute(sql, param, "DECIMAL");

        assertNotNull(result);
        assertTrue("结果应为 Map 类型", result instanceof Map);
    }

    /**
     * 测试 BOOLEAN 返回类型
     */
    public void testExecuteBooleanReturn() throws Exception {
        String sql = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END AS EXISTS_FLAG " +
                "FROM t_order WHERE order_id = :orderId";
        Map<String, Object> param = new HashMap<>();
        param.put("orderId", "ORD20260612001");

        Object result = executor.execute(sql, param, "BOOLEAN");

        assertNotNull(result);
        assertTrue("结果应为 Map 类型", result instanceof Map);
    }

    /**
     * 测试 DATE 返回类型
     */
    public void testExecuteDateReturn() throws Exception {
        String sql = "SELECT crt_time FROM t_order WHERE order_id = :orderId";
        Map<String, Object> param = new HashMap<>();
        param.put("orderId", "ORD20260612001");

        Object result = executor.execute(sql, param, "DATE");

        assertNotNull(result);
        assertTrue("结果应为 Map 类型", result instanceof Map);
    }

    /**
     * 测试不支持的返回类型应抛出 IllegalArgumentException
     */
    public void testExecuteUnsupportedReturnType() {
        String sql = "SELECT * FROM t_order";
        Map<String, Object> param = new HashMap<>();

        try {
            executor.execute(sql, param, "UNKNOWN_TYPE");
            fail("应抛出 IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue("异常信息应包含 '不支持的返回类型'",
                    e.getMessage().contains("不支持的返回类型"));
        } catch (Exception e) {
            fail("应抛出 IllegalArgumentException，实际抛出: " + e.getClass().getName());
        }
    }

    /**
     * 测试空结果集 LIST 返回
     */
    public void testExecuteListEmptyResult() throws Exception {
        String sql = "SELECT * FROM t_order WHERE member_id = :memberId";
        Map<String, Object> param = new HashMap<>();
        param.put("memberId", "NOT_EXIST_MEMBER");

        Object result = executor.execute(sql, param, "LIST");

        assertNotNull("空结果 LIST 不应为 null", result);
        assertTrue("结果应为 List 类型", result instanceof List);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = (List<Map<String, Object>>) result;
        assertEquals("不存在的数据应返回空列表", 0, list.size());
    }

    /**
     * 测试带参数的复杂查询 - 多条件筛选
     */
    public void testExecuteComplexConditionQuery() throws Exception {
        String sql = "SELECT order_id, order_amt, order_status FROM t_order " +
                "WHERE merchant_id = :merchantId AND order_status = :status " +
                "AND order_amt > :minAmt ORDER BY order_amt DESC";
        Map<String, Object> param = new HashMap<>();
        param.put("merchantId", "MER2001");
        param.put("status", 1);
        param.put("minAmt", 100.0);

        Object result = executor.execute(sql, param, "LIST");

        assertNotNull(result);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = (List<Map<String, Object>>) result;
        assertEquals("MER2001 且 status=1 且 amt>100 应有 1 条记录", 1, list.size());
        assertEquals("ORD20260612001", list.get(0).get("ORDER_ID"));
    }

    /**
     * 测试 NULL 参数值处理
     */
    public void testExecuteWithNullParam() throws Exception {
        String sql = "SELECT COUNT(*) AS CNT FROM t_order";
        // param 为空 Map
        Map<String, Object> param = new HashMap<>();

        Object result = executor.execute(sql, param, "LONG");

        assertNotNull(result);
        @SuppressWarnings("unchecked")
        Map<String, Object> row = (Map<String, Object>) result;
        assertEquals("总订单数应为 3", 3L, row.get("CNT"));
    }
}
