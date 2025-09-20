package com.thirty.user.enums.model;

import com.thirty.common.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType implements CodeEnum<Integer> {
    ALL(0),
    CHILD(1),
    GLOBAL(2),
    SELF(3),
    NOT_GLOBAL(4)
    ;
    private final Integer code;
}
