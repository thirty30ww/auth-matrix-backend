package com.thirty.user.service.basic;

import com.thirty.user.model.entity.Role;
import com.thirty.user.model.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【user_role(用户和角色表)】的数据库操作Service
* @createDate 2025-08-09 15:40:20
*/
public interface UserRoleService extends IService<UserRole> {

    /**
     * 根据用户ID查询角色名称列表
     * @param userId 用户ID
     * @return 角色名称列表
     */
    List<Role> getRolesByUserId(Integer userId);

    /**
     * 根据用户ID查询角色ID列表
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Integer> getRoleIds(Integer userId);

    /**
     * 根据用户ID列表查询角色ID列表
     * @param userIds 用户ID列表
     * @return 角色ID列表
     */
    List<Integer> getRoleIdsByUserIds(List<Integer> userIds);

    /**
     * 为用户添加角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     */
    void addUserRoles(Integer userId, List<Integer> roleIds);

    /**
     * 删除用户角色
     * @param userId 用户ID
     */
    void deleteUserRoles(Integer userId);

    /**
     * 更新用户角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     */
    void updateUserRoles(Integer userId, List<Integer> roleIds);
}
