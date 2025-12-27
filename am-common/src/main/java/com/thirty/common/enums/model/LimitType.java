package com.thirty.common.enums.model;

public enum LimitType {
    /**
     * 默认限流策略
     */
    DEFAULT,

    /**
     * IP限流策略
     */
    IP,

    /**
     * 令牌桶限流策略
     */
    TOKEN,

    /**
     * 自定义限流策略
     */
    CUSTOM
}
