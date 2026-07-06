package com.ly.ttd.biz.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.ly.ttd"})
@MapperScan({"com.ly.ttd.biz.admin.mybatis.mapper", "com.ly.ttd.biz.srv.*.mapper"})
public class ServerMain {


    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(ServerMain.class, args);
        System.out.println("service started success");
    }
}
