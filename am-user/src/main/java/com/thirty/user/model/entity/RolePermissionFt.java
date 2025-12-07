package com.thirty.user.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.thirty.user.model.entity.base.BaseRolePermission;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色前台权限表
 * @TableName role_permission_ft
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="role_permission_ft")
@Data
public class RolePermissionFt extends BaseRolePermission {
}