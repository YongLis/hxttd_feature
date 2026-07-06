package com.ly.ttd.connector.engine.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 输出映射器
 * 将连接器响应结果映射为指定的数据结构
 *
 * @author yong.li
 * @since 2026/04/15
 */
public class OutputMapper {

    private static final Logger logger = LoggerFactory.getLogger(OutputMapper.class);

    /**
     * 表达式正则：${response.data.score}
     */
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");

    /**
     * 应用输出映射
     *
     * @param response   响应对象
     * @param expression 映射表达式，如: "${res.data.score}"
     * @return 映射后的值
     */
    public Object applyMapping(Map<String, Object> response, String expression) {
        if (expression == null || expression.isEmpty()) {
            return response;
        }

        try {
            // 解析表达式
            Matcher matcher = EXPRESSION_PATTERN.matcher(expression);
            if (matcher.find()) {
                String path = matcher.group(1);
                return extractValue(response, path);
            }

            // 不是表达式，直接返回
            return expression;
        } catch (Exception e) {
            logger.warn("Failed to apply output mapping: expression={}, error={}", expression, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据路径提取值
     *
     * @param data 数据源
     * @param path 路径，如: "data.score"
     * @return 提取的值
     */
    private Object extractValue(Map<String, Object> data, String path) {
        if (data == null || path == null || path.isEmpty()) {
            return null;
        }

        String[] parts = path.split("\\.");
        Object current = data;

        for (String part : parts) {
            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(part);
                if (current == null) {
                    logger.debug("Path not found: {}", path);
                    return null;
                }
            } else {
                logger.warn("Cannot navigate path '{}' at part '{}'", path, part);
                return null;
            }
        }

        return current;
    }

    /**
     * 批量应用映射
     *
     * @param response 响应对象
     * @param mappings 映射配置，Key=字段名，Value=表达式
     * @return 映射后的结果
     */
    public Map<String, Object> applyBatchMapping(Map<String, Object> response, Map<String, String> mappings) {
        if (mappings == null || mappings.isEmpty()) {
            return response;
        }

        Map<String, Object> result = new java.util.HashMap<>();

        for (Map.Entry<String, String> entry : mappings.entrySet()) {
            String fieldName = entry.getKey();
            String expression = entry.getValue();

            result.put(fieldName, applyMapping(response, expression));
        }

        return result;
    }
}
