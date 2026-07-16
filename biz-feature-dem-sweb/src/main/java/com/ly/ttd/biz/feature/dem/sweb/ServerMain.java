package com.ly.ttd.biz.feature.dem.sweb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

/**
 * biz-feature-dem-sweb 服务启动类。
 */
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class,
        com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure.class
})
@Import(com.ly.ttd.daf.starter.MysqlRcsSlaveDataSourceAutoConfiguration.class)
@Slf4j
public class ServerMain {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ConfigurableApplicationContext ctx = SpringApplication.run(ServerMain.class, args);
        log.info("biz-feature-dem-sweb start success, cost={}ms", (System.currentTimeMillis() - start));

    }
}
