package com.thirty.user.enums.result;

import com.thirty.common.enums.IResult;
import com.thirty.common.enums.result.GlobalResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleResultCode implements IResult {
    ROLE_LIST_GET_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "角色列表获取成功"),
    ;

    private final Integer code;
    private final String message;
}
