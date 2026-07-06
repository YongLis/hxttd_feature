package com.ly.ttd.feature.sample;

import lombok.extern.slf4j.Slf4j;
import org.dromara.dynamictp.spring.annotation.EnableDynamicTp;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 特征引擎示例应用
 * <p>
 * 演示如何集成 ttd-feature-engine，启动后自动：
 * <ol>
 *   <li>通过 dynamic-tp 创建并管理线程池</li>
 *   <li>初始化 FeatureConfiguration Bean</li>
 *   <li>通过 SPI 加载连接器插件</li>
 *   <li>通过 ResourceLoader 实现加载元字段/指标/特征配置/接入点/数据集</li>
 * </ol>
 *
 * @author yong.li
 * @since 2026/6/29
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.ly.ttd"})
//@PropertySource("classpath:app.properties")
@MapperScan("com.ly.ttd.feature.sample.mybatis.mapper")
@EnableScheduling
@EnableDynamicTp
@Slf4j
public class SampleApplication {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        SpringApplication.run(SampleApplication.class, args);
        log.info("feature sample server start, cost={}ms", (System.currentTimeMillis()-start));

    }
}
