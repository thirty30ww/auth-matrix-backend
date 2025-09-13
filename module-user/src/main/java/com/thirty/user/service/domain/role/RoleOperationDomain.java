package com.thirty.user.service.domain.role;

import com.thirty.user.model.dto.RoleDTO;

import java.util.List;

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

    /**
     * 分配视图权限
     * @param roleId 角色ID
     * @param oldViewIds 旧视图ID列表
     * @param newViewIds 新视图ID列表
     */
    void assignView(Integer roleId, List<Integer> oldViewIds, List<Integer> newViewIds);

    /**
     * 获取角色视图ID列表
     * @param parentRoleId 父角色ID
     * @return 视图ID列表
     */
    List<Integer> getViewIdsByParentRoleId(Integer parentRoleId);
}
