package com.ly.ttd.connector.api.interceptor;

import com.ly.ttd.connector.api.ConnectorResponse;
import com.ly.ttd.connector.api.spi.ConnectorInterceptor;
import com.ly.ttd.connector.api.spi.ConnectorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志拦截器
 * 记录连接器请求和响应的详细信息
 *
 * @author yong.li
 * @since 2026/04/15
 */
public class LoggingInterceptor implements ConnectorInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public ConnectorResponse intercept(ConnectorInvocation invocation) throws Exception {
        ConnectorRequest<?> request = invocation.getRequest();

        // 前置日志
        logger.info("Connector Request: {}", request);
        long startTime = System.currentTimeMillis();

        try {
            // 执行调用
            ConnectorResponse response = invocation.proceed();

            // 计算耗时
            long cost = System.currentTimeMillis() - startTime;

            // 成功日志
            logger.info("Connector Response: cost={}ms, response={}", cost, response);

            return response;
        } catch (Exception e) {
            // 计算耗时
            long cost = System.currentTimeMillis() - startTime;

            // 异常日志
            logger.error("Connector Exception: cost={}ms, error={}", cost, e.getMessage(), e);

            throw e;
        }
    }
}
