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
}
