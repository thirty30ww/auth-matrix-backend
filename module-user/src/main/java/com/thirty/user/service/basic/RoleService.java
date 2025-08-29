package com.thirty.user.service.basic;

import com.baomidou.mybatisplus.extension.service.IService;
import com.thirty.user.model.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {
    /**
     * 获取最高级角色
     * @param roleIds 角色id列表
     * @return 最高级角色
     */
    Integer getHighestLevel(List<Integer> roleIds);

    /**
     * 获取用户列表
     * @param roleIds 角色id列表
     * @return 用户列表
     */
    List<Role> getRoles(List<Integer> roleIds);

    /**
     * 添加角色
     * @param role 角色
     */
    void addRole(Role role);

    /**
     * 更新角色
     * @param role 角色
     */
    void updateRole(Role role);
}
