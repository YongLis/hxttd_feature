package com.ly.ttd.connector.api.spi;

import com.ly.ttd.connector.api.ConnectorResponse;

/**
 * 连接器请求拦截器接口
 * 用于在请求执行前后进行拦截处理
 *
 * @author yong.li
 * @since 2026/04/15
 */
public interface ConnectorInterceptor {

    /**
     * 拦截器方法
     *
     * @param invocation 调用上下文
     * @return 响应结果
     * @throws Exception 异常
     */
    ConnectorResponse intercept(ConnectorInvocation invocation) throws Exception;

    /**
     * 调用上下文
     */
    interface ConnectorInvocation {
        /**
         * 获取请求对象
         *
         * @return 请求对象
         */
        ConnectorRequest<?> getRequest();

        /**
         * 执行下一个拦截器或真实调用
         *
         * @return 响应结果
         * @throws Exception 异常
         */
        ConnectorResponse proceed() throws Exception;
    }
}
