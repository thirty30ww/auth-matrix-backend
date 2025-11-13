package com.thirty.user.service.domain.role;

import com.thirty.user.model.entity.Role;

import java.util.List;

public interface RoleQueryDomain {
    /**
     * 获取角色列表
     * @return 角色列表
     */
    List<Role> getRoles();

    /**
     * 根据用户ID查询角色ID列表
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Integer> getRoleIds(Integer userId);

    /**
     * 获取用户角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> getUserRoles(Integer userId);

    /**
     * 获取全局角色列表
     * @return 全局角色列表
     */
    List<Role> getGlobalRoles();

    /**
     * 获取子角色列表
     * @param userId 用户ID
     * @return 子角色列表
     */
    List<Role> getChildRoles(Integer userId);

    /**
     * 获取子角色ID列表（包含自身）
     * @param roleId 角色ID
     * @return 子角色ID列表
     */
    List<Integer> getChildAndSelfRoleIds(Integer roleId);

    /**
     * 获取子角色ID列表（包含自身）
     * @param roleIds 角色ID列表
     * @return 子角色ID列表
     */
    List<Integer> getChildAndSelfRoleIds(List<Integer> roleIds);

    /**
     * 获取子角色ID列表（不包含自身）
     * @param roleIds 角色ID列表
     * @return 子角色ID列表
     */
    List<Integer> getChildRoleIds(List<Integer> roleIds);
}
