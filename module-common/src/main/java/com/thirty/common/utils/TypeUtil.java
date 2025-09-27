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
        } else if (targetType == Double.class) {
            return (T) Double.valueOf(value.toString());
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

    /**
     * 带类型校验的转换为字符串方法
     * @param value 要转换的值
     * @param expectedType 期望的类型
     * @return 转换后的字符串
     * @throws IllegalArgumentException 当类型不匹配时抛出异常
     */
    public String convertToStringWithValidation(Object value, Class<?> expectedType) {
        if (value == null) {
            return null;
        }
        
        // 进行类型校验
        validateType(value, expectedType);
        
        // 校验通过后进行转换
        return convertToString(value);
    }

    /**
     * 校验值的类型是否匹配期望类型
     * @param value 要校验的值
     * @param expectedType 期望的类型
     * @throws IllegalArgumentException 当类型不匹配时抛出异常
     */
    private void validateType(Object value, Class<?> expectedType) {
        if (value == null) {
            return;
        }
        
        if (expectedType == Boolean.class) {
            validateBooleanType(value);
        } else if (expectedType == Integer.class) {
            validateIntegerType(value);
        } else if (expectedType == Double.class) {
            validateDoubleType(value);
        } else if (expectedType == String.class) {
            validateStringType(value);
        } else {
            validateOtherType(value, expectedType);
        }
    }

    /**
     * 校验布尔类型
     * @param value 要校验的值
     * @throws IllegalArgumentException 当类型不匹配时抛出异常
     */
    private void validateBooleanType(Object value) {
        if (!(value instanceof Boolean)) {
            String stringValue = value.toString().toLowerCase();
            if (!"true".equals(stringValue) && !"false".equals(stringValue) && 
                !"1".equals(stringValue) && !"0".equals(stringValue)) {
                throw new IllegalArgumentException("值无法转换为布尔类型: " + value);
            }
        }
    }

    /**
     * 校验整数类型
     * @param value 要校验的值
     * @throws IllegalArgumentException 当类型不匹配时抛出异常
     */
    private void validateIntegerType(Object value) {
        if (!(value instanceof Integer)) {
            if (value instanceof Number) {
                // 其他数字类型，检查是否为整数
                double doubleValue = ((Number) value).doubleValue();
                if (doubleValue != Math.floor(doubleValue)) {
                    throw new IllegalArgumentException("数值不是整数: " + value);
                }
            } else {
                // 尝试解析字符串
                try {
                    Integer.valueOf(value.toString());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("值无法转换为整数类型: " + value, e);
                }
            }
        }
    }

    /**
     * 校验双精度浮点数类型
     * @param value 要校验的值
     * @throws IllegalArgumentException 当类型不匹配时抛出异常
     */
    private void validateDoubleType(Object value) {
        if (!(value instanceof Double)) {
            if (value instanceof Number) {
                // 其他数字类型，直接允许（可以转换为Double）
                // 无需额外校验
            } else {
                // 尝试解析字符串
                try {
                    Double.valueOf(value.toString());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("值无法转换为双精度浮点数类型: " + value, e);
                }
            }
        }
    }

    /**
     * 校验字符串类型
     * @param value 要校验的值
     */
    private void validateStringType(Object value) {
        // 字符串类型：任何非null值都可以转换为字符串
        // 无需特殊校验
    }

    /**
     * 校验其他类型
     * @param value 要校验的值
     * @param expectedType 期望的类型
     * @throws IllegalArgumentException 当类型不匹配时抛出异常
     */
    private void validateOtherType(Object value, Class<?> expectedType) {
        if (!expectedType.isInstance(value)) {
            throw new IllegalArgumentException(
                String.format("期望类型 %s，但实际类型为 %s", 
                    expectedType.getSimpleName(), value.getClass().getSimpleName()));
        }
    }
}
