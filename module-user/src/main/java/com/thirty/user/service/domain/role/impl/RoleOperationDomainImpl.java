package com.thirty.user.service.domain.role.impl;

import com.thirty.user.converter.RoleConverter;
import com.thirty.user.model.dto.RoleDTO;
import com.thirty.user.model.entity.Role;
import com.thirty.user.service.basic.RoleService;
import com.thirty.user.service.domain.role.RoleOperationDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RoleOperationDomainImpl implements RoleOperationDomain {

    @Resource
    private RoleService roleService;

    /**
     * 添加角色
     * @param roleDTO 角色dto
     */
    @Override
    public void addRole(RoleDTO roleDTO) {
        Role role = RoleConverter.INSTANCE.toRole(roleDTO);
        roleService.addRole(role);
    }

    /**
     * 更新角色
     * @param roleDTO 角色dto
     */
    @Override
    public void updateRole(RoleDTO roleDTO) {
        Role role = RoleConverter.INSTANCE.toRole(roleDTO);
        roleService.updateRole(role);
    }

    /**
     * 删除角色
     * @param roleId 角色ID
     */
    @Override
    public void deleteRole(Integer roleId) {
        roleService.removeById(roleId);
    }
}
