package com.thirty.common.utils;

import org.springframework.stereotype.Component;

@Component
public class TypeUtil {

    @SuppressWarnings("unchecked")
    public <T> T convertValue(Object value, Class<T> targetType) {
        if (value == null) {
            return null;
        }
        if (targetType == Boolean.class) {
            return (T) Boolean.valueOf("1".equals(value));
        } else if (targetType == Integer.class) {
            return (T) Integer.valueOf(value.toString());
        } else {
            return (T) value.toString();
        }
    }

    /**
     * 将各种类型的值转换为字符串
     * @param value 要转换的值
     * @return 转换后的字符串
     */
    public String convertToString(Object value) {
        if (value == null) {
            return null;
        }
        
        if (value instanceof Boolean) {
            // 布尔值转换：true -> "1", false -> "0"
            return ((Boolean) value) ? "1" : "0";
        } else if (value instanceof Number) {
            // 数字类型直接转换为字符串
            return value.toString();
        } else if (value instanceof String) {
            // 字符串直接返回
            return (String) value;
        } else {
            // 其他类型调用toString()方法
            return value.toString();
        }
    }

    /**
     * 将各种类型的值转换为字符串（支持自定义布尔值转换）
     * @param value 要转换的值
     * @param trueValue 布尔true值对应的字符串
     * @param falseValue 布尔false值对应的字符串
     * @return 转换后的字符串
     */
    public String convertToString(Object value, String trueValue, String falseValue) {
        if (value == null) {
            return null;
        }
        
        if (value instanceof Boolean) {
            return ((Boolean) value) ? trueValue : falseValue;
        } else {
            return convertToString(value);
        }
    }
}
