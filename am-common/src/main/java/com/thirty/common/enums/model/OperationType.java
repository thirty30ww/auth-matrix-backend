package com.thirty.common.enums.model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.thirty.common.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationType implements CodeEnum<String> {
    SELECT("SELECT"),
    INSERT("INSERT"),
    UPDATE("UPDATE"),
    DELETE("DELETE"),
    UPLOAD("UPLOAD"),
    SEND("SEND"),
    ;

    @EnumValue
    @JsonValue
    private final String code;

    /**
     * 根据编码获取枚举
     * @param code 编码
     * @return 枚举实例
     */
    public static OperationType getByCode(String code) {
        return CodeEnum.getByCode(OperationType.class, code);
    }
}
