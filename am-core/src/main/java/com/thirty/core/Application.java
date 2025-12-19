package com.thirty.core;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(
    scanBasePackages = {
        // 直接扫描整个 com.thirty 包
        "com.thirty"
    }
    ,exclude = {
        // 排除Redis Repository自动配置，项目使用的是MyBatis Plus，不需要Spring Data Redis Repository功能
        RedisRepositoriesAutoConfiguration.class,
        // 排除Spring Data Web自动配置，这是projectingArgumentResolverBeanPostProcessor的来源
        SpringDataWebAutoConfiguration.class
    }
)
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.thirty.**.mapper") // 使用通配符扫描所有模块的 mapper
@EnableTransactionManagement
@Slf4j
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
