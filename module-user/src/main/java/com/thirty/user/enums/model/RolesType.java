package com.thirty.user.enums.model;

import com.thirty.common.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RolesType implements CodeEnum<Integer> {
    /**
     * 所有角色
     */
    ALL(0),
    /**
     * 子角色
     */
    CHILD(1),
    /**
     * 子角色和全局角色
     */
    CHILD_AND_GLOBAL(2),
    /**
     * 全局角色
     */
    GLOBAL(3),
    /**
     * 子角色和当前用户角色
     */
    CHILD_AND_SELF(4),
    /**
     * 非全局角色
     */
    NOT_GLOBAL(5),
    ;

    private final Integer code;

    public static RolesType getByCode(Integer code) {
        return CodeEnum.getByCode(RolesType.class, code);
    }
}
