package com.thirty.common.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

import static com.thirty.common.constant.DateConstant.DATE_TIME_FORMAT;

/**
 * Jackson配置类
 * 用于配置JSON序列化和反序列化格式
 */
@Configuration
public class JacksonConfig {
    /**
     * 配置LocalDateTime格式化
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // 配置LocalDateTime的序列化和反序列化格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
            builder.serializers(new LocalDateTimeSerializer(formatter));
            builder.deserializers(new LocalDateTimeDeserializer(formatter));
            
            // 配置枚举处理：只允许空字符串当作null，未知枚举值仍然抛出异常
            builder.featuresToEnable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

            // 启用JSON格式化输出 - 让所有JSON都会换行
            builder.featuresToEnable(SerializationFeature.INDENT_OUTPUT);
        };
    }
}