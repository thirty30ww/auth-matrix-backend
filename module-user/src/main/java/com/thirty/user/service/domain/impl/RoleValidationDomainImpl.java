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
     * 校验用户ID列表是否都包含于子角色
     * @param username 用户名
     * @param userIds 用户ID列表
     * @return 是：true，否：false
     */
    @Override
    public boolean validateUserIdsRolesContainChildRoles(String username, List<Integer> userIds) {
        List<Integer> roleIds = userRoleService.getRoleIdsByUserIds(userIds);
        return validateRolesContainChildRoles(username, roleIds);
    }

    /**
     * 校验角色ID列表是否都包含于子角色
     * @param username 用户名
     * @param roleIds 角色ID列表
     * @return 是：true，否：false
     */
    @Override
    public boolean validateRolesContainChildRoles(String username, List<Integer> roleIds) {
        // 获取当前用户有权限的角色ID列表
        List<Role> permittedRoles = roleQueryDomain.getChildRoles(username);
        List<Integer> permittedRoleIds = Role.extractIds(permittedRoles);

        // 检查roleIds是否全都包含在roleList内
        for (Integer roleId : roleIds) {
            if (!permittedRoleIds.contains(roleId)) {
                return false;
            }
        }

        return true;
    }
}
