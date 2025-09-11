package com.thirty.user.enums.result;

import com.thirty.common.enums.IResult;
import com.thirty.common.enums.result.GlobalResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ViewResultCode implements IResult {

    GET_VIEW_TREE_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取视图树成功"),
    GET_MENU_TREE_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取菜单树成功"),
    GET_LIST_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取页面列表成功"),
    GET_DIRECTORY_TREE_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取目录树成功"),
    GET_MENU_AND_BUTTON_TREE_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取菜单和按钮树成功"),
    GET_PERMISSION_CODE_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "获取权限码列表成功"),
    ADD_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "添加成功"),
    MODIFY_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "修改成功"),
    DELETE_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "删除成功"),
    MOVE_SUCCESS(GlobalResultCode.SUCCESS.getCode(), "移动成功"),

    VIEW_NOT_AUTHORIZED_ASSIGN(5001, "您没有权限分配该菜单"),
    VIEW_NOT_AUTHORIZED_ADD(5002, "您没有权限添加该菜单"),
    VIEW_NOT_AUTHORIZED_DELETE(5003, "您没有权限删除该菜单"),
    VIEW_NOT_AUTHORIZED_MODIFY(5004, "您没有权限修改该菜单"),
    VIEW_TYPE_NOT_COMPLY(5005, "视图类型不符合父节点类型"),
    VIEW_CANNOT_BE_PARENT(5006, "视图不能作为自己的父节点"),
    VIEW_CANNOT_MOVE_UP(5007, "视图不能上移"),
    VIEW_CANNOT_MOVE_DOWN(5008, "视图不能下移"),
    VIEW_CANNOT_MODIFY_VALID(5009, "视图状态不能修改"),
    ;

    private final Integer code;
    private final String message;
}
