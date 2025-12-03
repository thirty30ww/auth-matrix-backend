package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.thirty.user.enums.model.PermissionBkType;
import com.thirty.user.model.entity.base.BasePermission;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 页面表
 * @TableName view
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="permission_bk")
@Data
public class PermissionBk extends BasePermission {
    /**
     * 菜单的图标
     */
    private String icon;

    /**
     * 页面类型(1:目录, 2:菜单, 3:页面)
     */
    private PermissionBkType type;

    /**
     * 构建无效权限列表
     * @param permissionIds 权限ID列表
     * @return 无效权限列表
     */
    public static List<PermissionBk> toNotValidPermission(List<Integer> permissionIds) {
        return permissionIds.stream().map(permissionId -> {
            PermissionBk permissionBk = new PermissionBk();
            permissionBk.setId(permissionId);
            permissionBk.setIsValid(false);
            return permissionBk;
        }).collect(Collectors.toList());
    }
}