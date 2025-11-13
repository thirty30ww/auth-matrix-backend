package com.thirty.user.enums.model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.thirty.common.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoginType implements CodeEnum<String> {
    LOGIN("LOGIN", "登录"),
    REGISTER("REGISTER", "注册"),
    LOGOUT("LOGOUT", "退出登录"),
    REFRESH("REFRESH", "刷新token"),
    ;

    @EnumValue
    private final String code;
    @JsonValue
    private final String description;

    /**
     * 根据编码获取枚举
     * @param code 编码
     * @return 枚举实例
     */
    public LoginType getByCode(String code) {
        return CodeEnum.getByCode(LoginType.class, code);
    }
}
