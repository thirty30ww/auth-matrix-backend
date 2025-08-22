package com.thirty.core.aspect;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 自动数据源切换切面
 * 根据包路径自动选择对应的数据源
 */
@Aspect
@Component
@Order(1) // 确保在事务之前执行
@Slf4j
public class AutoDataSourceAspect {
    @Before("execution(* com.thirty.common.service..*(..))")
    public void setCommonDataSource() {
        DynamicDataSourceContextHolder.push("common");
    }

    @Before("execution(* com.thirty.user.service..*(..))")
    public void setUserDataSource() {
        DynamicDataSourceContextHolder.push("user");
    }

    @After("execution(* com.thirty.common.service..*(..))")
    public void clearCommonDataSource() {
        DynamicDataSourceContextHolder.clear();
    }

    @After("execution(* com.thirty.user.service..*(..))")
    public void clearUserDataSource() {
        DynamicDataSourceContextHolder.clear();
    }
}
