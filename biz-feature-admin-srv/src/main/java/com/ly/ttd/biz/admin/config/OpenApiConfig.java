package com.ly.ttd.biz.admin.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * SpringDoc OpenAPI 配置
 *
 * @author yong.li
 * @since 2026-07-01
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TTD 运营支撑平台 API 文档")
                        .description("""
                                TTD 统一特征平台运营支撑后台接口文档。

                                ## 模块分类
                                - **特征配置** — 实时特征定义、时间窗口、聚合配置
                                - **指标管理** — 指标定义、血缘关系
                                - **连接器管理** — JDBC/ES/HBase/HTTP 数据源连接器
                                - **元字段管理** — 事件元字段脚本与测试
                                - **接入点管理** — 业务接入点注册与文档
                                - **审核管理** — 特征/指标/连接器审批流程
                                - **系统配置** — 项目/账户/字典/文件
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("TTD Feature Team")
                                .email("ttd-feature@ly.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:7082").description("本地开发环境"),
                        new Server().url("http://dev.ttd.com").description("开发环境"),
                        new Server().url("http://test.ttd.com").description("测试环境")
                ));
    }
}
