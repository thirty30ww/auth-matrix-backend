package com.thirty.user.service.domain.impl;

import com.thirty.user.model.entity.Role;
import com.thirty.user.service.basic.UserRoleService;
import com.thirty.user.service.domain.RoleValidationDomain;
import com.thirty.user.service.domain.RoleQueryDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleValidationDomainImpl implements RoleValidationDomain {

    @Resource
    private UserRoleService userRoleService;
    @Resource
    private RoleQueryDomain roleQueryDomain;

    /**
     * 校验用户ID是否包含于当前用户的子角色
     * @param currentUserId 当前用户ID
     * @param targetUserId 目标用户ID
     * @return 是：true，否：false
     */
    @Override
    public boolean validateUserIdRolesContainChildRoles(Integer currentUserId, Integer targetUserId) {
        List<Integer> userIds = List.of(targetUserId);
        return validateUserIdsRolesContainChildRoles(currentUserId, userIds);
    }

    /**
     * 校验用户ID列表是否都包含于当前用户的子角色
     * @param currentUserId 用户ID
     * @param targetUserIds 用户ID列表
     * @return 是：true，否：false
     */
    @Override
    public boolean validateUserIdsRolesContainChildRoles(Integer currentUserId, List<Integer> targetUserIds) {
        List<Integer> roleIds = userRoleService.getRoleIdsByUserIds(targetUserIds);
        return validateRolesContainChildRoles(currentUserId, roleIds);
    }

    /**
     * 校验角色ID列表是否都包含于子角色
     * @param currentUserId 用户ID
     * @param targetRoleIds 角色ID列表
     * @return 是：true，否：false
     */
    @Override
    public boolean validateRolesContainChildRoles(Integer currentUserId, List<Integer> targetRoleIds) {
        // 获取当前用户有权限的角色ID列表
        List<Role> permittedRoles = roleQueryDomain.getChildRoles(currentUserId);
        List<Integer> permittedRoleIds = Role.extractIds(permittedRoles);

        // 检查roleIds是否全都包含在targetRoleIds内
        for (Integer roleId : targetRoleIds) {
            if (!permittedRoleIds.contains(roleId)) {
                return false;
            }
        }

        return true;
    }
}
