package com.thirty.user.enums.result;

import com.thirty.common.enums.IResult;
import com.thirty.common.enums.result.GlobalResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleResultCode implements IResult {
    ROLE_NOT_AUTHORIZED_ADD(4001, "您没有权限添加该角色"),
    ROLE_NOT_AUTHORIZED_UPDATE(4002, "您没有权限更新该角色"),
    ROLE_NOT_AUTHORIZED_DELETE(4003, "您没有权限删除该角色"),

    ROLE_LIST_GET_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取角色列表成功"),
    ROLE_TREE_GET_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取角色树成功"),
    ROLE_ADD_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "添加角色成功"),
    ROLE_UPDATE_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "更新角色成功"),
    ROLE_DELETE_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "删除角色成功"),
    ;

    private final Integer code;
    private final String message;
}
