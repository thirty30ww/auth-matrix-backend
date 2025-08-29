package com.thirty.user.service.domain.role;

import com.thirty.user.model.dto.RoleDTO;

public interface RoleOperationDomain {
    /**
     * 添加角色
     * @param roleDTO 角色dto
     */
    void addRole(RoleDTO roleDTO);

    /**
     * 更新角色
     * @param roleDTO 角色dto
     */
    void updateRole(RoleDTO roleDTO);

    /**
     * 删除角色
     * @param roleId 角色ID
     */
    void deleteRole(Integer roleId);
}
