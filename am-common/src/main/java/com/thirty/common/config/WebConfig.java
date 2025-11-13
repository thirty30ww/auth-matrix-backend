package com.thirty.common.config;

import com.thirty.common.converter.StringToCodeEnumConverterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 添加自定义转换器
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 注册 CodeEnum 转换器工厂
        registry.addConverterFactory(new StringToCodeEnumConverterFactory());
    }
}