package com.thirty.common.annotation;

import com.thirty.common.enums.model.LimitType;

import java.lang.annotation.*;

/**
 * 限流注解
 * 用于方法或类上，对其进行限流控制
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    /**
     * 限流key
     */
    String key() default "";

    /**
     * 限流时间，单位：秒
     */
    int time() default 60;

    /**
     * 限流次数
     */
    int count() default 100;

    /**
     * 限流策略
     */
    LimitType limitType() default LimitType.DEFAULT;

    /**
     * 限流提示消息
     */
    String message() default "访问过于频繁，请稍后再试";
}

