package com.thirty.common.enums.model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.thirty.common.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public enum MethodType implements CodeEnum<String> {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    PATCH("PATCH"),
    ;

    @EnumValue
    @JsonValue
    private final String code;

    /**
     * 根据编码获取枚举
     * @param code 编码
     * @return 枚举实例
     */
    public static MethodType getByCode(String code) {
        return CodeEnum.getByCode(MethodType.class, code);
    }

    /**
     * 获取需要参数的方法类型列表
     * @return 方法类型列表
     */
    public static List<MethodType> getQueryMethodTypes() {
        return List.of(GET, DELETE);
    }

    /**
     * 获取需要请求体的方法类型列表
     * @return 方法类型列表
     */
    public static List<MethodType> getBodyMethodTypes() {
        return List.of(POST, PUT, PATCH);
    }
}
