package com.thirty.core;

import io.github.cdimascio.dotenv.Dotenv;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;

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
@MapperScan("com.thirty.**.mapper") // 使用通配符扫描所有模块的 mapper
public class ModuleCoreApplication {
    public static void main(String[] args) {
        // 加载.env文件
        Dotenv dotenv = Dotenv.configure()
                .directory(".")  // 在项目根目录查找.env文件
                .ignoreIfMissing()  // 如果.env文件不存在，不报错
                .load();
        
        // 将.env中的变量设置为系统环境变量
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });
        
        SpringApplication.run(ModuleCoreApplication.class, args);
    }
}
