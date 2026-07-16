package com.ly.ttd.biz.feature.admin;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.ly.ttd"})
@MapperScan({"com.ly.ttd.biz.feature.admin.mybatis.mapper"})
@Slf4j
public class ServerMain {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        ConfigurableApplicationContext context = SpringApplication.run(ServerMain.class, args);
        log.info("biz-feature-admin-srv started success, cost={}ms", (System.currentTimeMillis() - start));
    }
}
