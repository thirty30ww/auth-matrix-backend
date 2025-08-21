package com.thirty.user.enums.result;

import com.thirty.common.enums.IResult;
import com.thirty.common.enums.result.GlobalResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserResultCode implements IResult {
    USER_NOT_EXISTS(2001, "用户不存在"),
    USER_ID_MISMATCH(2002, "用户不匹配"),
    PASSWORD_MISMATCH(2003, "新密码与确认密码不匹配"),
    USER_INFO_UPDATE_FAILED(2004, "用户信息更新失败"),
    CURRENT_PASSWORD_INCORRECT(2005, "当前密码错误"),
    USER_ROLE_NOT_FOUND(2006, "用户角色不存在"),
    ROLE_NOT_AUTHORIZED_ADD(2007, "存在没有权限添加的角色"),
    ROLE_NOT_AUTHORIZED_MODIFY(2008, "存在没有权限修改的角色"),
    ROLE_NOT_AUTHORIZED_BAN(2009, "存在没有权限封禁的角色"),
    ROLE_NOT_AUTHORIZED_UNBAN(2010, "存在没有权限解封的角色"),
    USER_BANNED(2011, "用户被封禁, 请联系管理员解封"),

    USER_ADD_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "添加成功"),
    USER_INFO_GET_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取成功"),
    USER_MODIFY_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "修改成功"),
    USER_INFO_UPDATE_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "更新成功"),
    CHANGE_PASSWORD_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "修改成功"),
    USER_BAN_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "封禁成功"),
    USER_UNBAN_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "解封成功"),
    USER_LIST_GET_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "用户列表获取成功"),
    ;

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String message;
}
