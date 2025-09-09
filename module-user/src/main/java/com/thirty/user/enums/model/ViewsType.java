package com.thirty.user.enums.model;

import com.thirty.common.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ViewsType {
    /**
     * 视图
     */
    VIEW(0),
    /**
     * 菜单
     */
    MENU(1),
    /**
     * 菜单和按钮
     */
    MENU_AND_BUTTON(2),
    /**
     * 页面
     */
    PAGE(3),
    /**
     * 目录
     */
    DIRECTORY(4),
    ;

    private final Integer code;

    public static RolesType getByCode(Integer code) {
        return CodeEnum.getByCode(RolesType.class, code);
    }
}
