package com.ly.ttd.biz.feature.data.srv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * biz-feature-dem-srv 服务启动类。
 * <p>
 * 启动后自动:
 * <ul>
 *   <li>启动嵌入式 Undertow RPC 服务器 (端口 18080)</li>
 *   <li>将标注 {@code @RpcService} 的服务实现注册到 Nacos</li>
 *   <li>为标注 {@code @Rpcwired} 的字段注入远程服务代理</li>
 * </ul>
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
