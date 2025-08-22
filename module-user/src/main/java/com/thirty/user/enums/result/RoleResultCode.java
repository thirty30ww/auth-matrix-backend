package com.thirty.user.enums.result;

import com.thirty.common.enums.IResult;
import com.thirty.common.enums.result.GlobalResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleResultCode implements IResult {
    ROLE_LIST_GET_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取角色列表成功"),
    ROLE_TREE_GET_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取角色树成功"),
    ;

    private final Integer code;
    private final String message;
}
