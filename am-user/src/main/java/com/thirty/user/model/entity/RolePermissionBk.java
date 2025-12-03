package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.thirty.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色页面表
 * @TableName role_view
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="role_permission_bk")
@Data
public class RolePermissionBk extends BaseEntity {
    /**
     * 角色ID（关联role表）
     */
    private Integer roleId;

    /**
     * 页面ID（关联permission_bk表）
     */
    private Integer permissionId;

    /**
     * 从角色权限列表中提取权限id列表
     * @param rolePermissionBks 角色权限列表
     * @return 权限id列表
     */
    public static List<Integer> extractPermissionIds(List<RolePermissionBk> rolePermissionBks) {
        return rolePermissionBks.stream().map(RolePermissionBk::getPermissionId).distinct().collect(Collectors.toList());
    }

    /**
     * 拼接角色权限id列表（返回格式roleId_viewId）
     * @param rolePermissionBks 角色权限列表
     * @return 拼接后的角色权限id列表
     */
    public static List<String> spliceRolePermissionIds(List<RolePermissionBk> rolePermissionBks) {
        return rolePermissionBks.stream().map(rv -> rv.getRoleId() + "_" + rv.getPermissionId()).distinct().collect(Collectors.toList());
    }
}