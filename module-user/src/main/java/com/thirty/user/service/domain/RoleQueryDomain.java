package com.thirty.user.service.domain;

import com.thirty.user.model.entity.Role;
import com.thirty.user.model.vo.RoleVO;

import java.util.List;

public interface RoleQueryDomain {
    /**
     * 获取角色列表
     * @return 角色列表
     */
    List<Role> getRoles();

    /**
     * 获取子角色列表
     * @param username 用户名
     * @return 子角色列表
     */
    List<Role> getChildRoles(String username);

    /**
     * 获取子角色ID列表
     * @param username 用户名
     * @return 子角色ID列表
     */
    List<Integer> getChildRoleIds(String username);

    /**
     * 获取角色VO列表
     * @param username 用户名
     * @param hasPermissionDisplay 是否仅显示有权限操作的角色
     * @return 角色VO列表
     */
    List<RoleVO> getRoleTree(String username, boolean hasPermissionDisplay);

    /**
     * 获取全局角色列表
     * @return 全局角色列表
     */
    List<Role> getGlobalRoles();
}
