package com.thirty.common.enums;

/**
 * 结果码接口
 * 所有枚举类都应该实现此接口
 */
public interface IResult {
    /**
     * 获取状态码
     * @return 状态码
     */
    Integer getCode();
    
    /**
     * 获取消息
     * @return 消息
     */
    String getMessage();
} 