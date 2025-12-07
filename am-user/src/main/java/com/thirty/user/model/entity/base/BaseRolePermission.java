package com.thirty.user.model.entity.base;

import com.thirty.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseRolePermission extends BaseEntity {
    /**
     * 角色ID（关联role表）
     */
    private Integer roleId;

     /**
      * 权限ID（关联permission表）
      */
    private Integer permissionId;

    /**
     * 从角色权限列表中提取权限ID列表（去重）
     * @param rolePermissions 角色权限列表
     * @return 权限ID列表（去重）
     */
    public static <T extends BaseRolePermission> List<Integer> extractPermissionIds(List<T> rolePermissions) {
        return rolePermissions.stream().map(BaseRolePermission::getPermissionId).distinct().collect(Collectors.toList());
    }

    /**
     * 拼接角色权限id列表（返回格式roleId_viewId）
     * @param rolePermissions 角色权限列表
     * @return 角色权限id列表（去重）
     */
    public static <T extends BaseRolePermission> List<String> spliceRolePermissionIds(List<T> rolePermissions) {
        return rolePermissions.stream().map(rv -> rv.getRoleId() + "_" + rv.getPermissionId()).distinct().collect(Collectors.toList());
    }
}
