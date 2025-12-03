package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色页面表
 * @TableName role_view
 */
@TableName(value ="role_permission_bk")
@Data
public class RolePermissionBk implements Serializable {
    /**
     * 角色页面ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 角色ID（关联role表）
     */
    private Integer roleId;

    /**
     * 页面ID（关联permission_bk表）
     */
    private Integer permissionId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否被删除(1:是 0:否)
     */
    @TableLogic
    private Boolean isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

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