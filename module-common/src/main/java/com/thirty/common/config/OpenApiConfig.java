package com.thirty.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * SpringDoc OpenAPI 配置类
 * 用于配置API文档的生成和展示
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")  // 从配置文件读取端口号，默认8080
    private String serverPort;

    @Value("${project.info.title}")
    private String projectTitle;

    @Value("${project.info.version}")
    private String projectVersion;

    @Value("${project.info.author}")
    private String projectAuthor;

    @Value("${project.info.email}")
    private String projectEmail;

    /**
     * 配置全局OpenAPI信息
     * 必要 - 定义API文档的基本信息和安全认证方式
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // 配置API基本信息 - 必要
                .info(new Info()
                        .title(projectTitle + " API")  // API标题 - 必要
                        .description(projectTitle + "后端API接口文档")  // API描述 - 可选，但建议添加
                        .version(projectVersion)  // API版本 - 必要
                        .contact(new Contact()  // 联系信息 - 可选
                                .name(projectAuthor)
                                .email(projectEmail)))
                // 配置服务器信息 - 可选，但建议添加以便测试
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("本地开发环境"),
                        new Server()
                                .url("http://106.53.19.163:" + serverPort)
                                .description("生产环境")))
                // 配置全局安全认证 - 必要（因为项目使用JWT）
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .name("Bearer Authentication")  // 认证方案名称
                                        .type(SecurityScheme.Type.HTTP)  // 认证类型为HTTP
                                        .scheme("bearer")  // 使用bearer token
                                        .bearerFormat("JWT")  // token格式为JWT
                                        .description("请输入JWT token，格式：Bearer <token>")));
    }

    /**
     * 用户模块API分组
     * 可选 - 用于将API按模块分组展示，便于管理
     */
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("用户模块")  // 分组名称
                .pathsToMatch("/user/**", "/auth/**", "/role/**", "/permission/**", "/log/**")  // 匹配的路径
                .build();
    }

    /**
     * 系统模块API分组
     * 可选 - 用于将API按模块分组展示，便于管理
     */
    @Bean
    public GroupedOpenApi systemApi() {
        return GroupedOpenApi.builder()
                .group("系统模块")  // 分组名称
                .pathsToMatch("/system/**", "/file/**", "/setting/**")  // 匹配的路径
                .build();
    }
}