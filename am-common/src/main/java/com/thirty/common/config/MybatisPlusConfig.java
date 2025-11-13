package com.thirty.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.thirty.common.constant.LogicDeleteConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus配置类
 * 分页插件
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 全局配置
     * 配置逻辑删除
     */
    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        // 逻辑删除字段名
        dbConfig.setLogicDeleteField(LogicDeleteConstant.LOGIC_DELETE_FIELD);
        // 逻辑删除值（删除后的值）
        dbConfig.setLogicDeleteValue(LogicDeleteConstant.LOGIC_DELETE_VALUE);
        // 逻辑未删除值（删除前的值）
        dbConfig.setLogicNotDeleteValue(LogicDeleteConstant.LOGIC_NOT_DELETE_VALUE);

        // 字段策略配置 - 忽略null值
        dbConfig.setWhereStrategy(com.baomidou.mybatisplus.annotation.FieldStrategy.IGNORED);
        globalConfig.setDbConfig(dbConfig);
        return globalConfig;
    }
}