package com.thirty.user.service.domain.role.impl;

import com.thirty.user.model.entity.Role;
import com.thirty.user.service.basic.RoleService;
import com.thirty.user.service.basic.UserRoleService;
import com.thirty.user.service.domain.role.RoleQueryDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleQueryDomainImpl implements RoleQueryDomain {
    @Resource
    private RoleService roleService;
    @Resource
    private UserRoleService userRoleService;

    /**
     * 获取角色列表
     * @return 角色列表
     */
    @Override
    public List<Role> getRoles() {
        return roleService.list();
    }

    /**
     * 根据用户ID查询角色ID列表
     * @param userId 用户ID
     * @return 角色ID列表
     */
    @Override
    public List<Integer> getRoleIds(Integer userId) {
        return userRoleService.getRoleIds(userId);
    }

    /**
     * 获取用户角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<Role> getUserRoles(Integer userId) {
        return userRoleService.getRolesByUserId(userId);
    }

    /**
     * 获取子角色列表
     * @param userId 用户ID
     * @return 子角色列表
     */
    @Override
    public List<Role> getChildRoles(Integer userId) {
        // 获取用户角色
        List<Integer> parentRoleIds = getRoleIds(userId);

        // 获取子角色
        return roleService.getChildRoles(parentRoleIds);
    }

    /**
     * 获取全局角色列表
     * @return 全局角色列表
     */
    @Override
    public List<Role> getGlobalRoles() {
        List<Role> allRoles = roleService.list();
        return Role.getGlobalRoles(allRoles);
    }


}
