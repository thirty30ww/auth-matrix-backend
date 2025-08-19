package com.thirty.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.common.exception.BusinessException;
import com.thirty.user.enums.result.UserResultCode;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.entity.UserRole;
import com.thirty.user.service.UserRoleService;
import com.thirty.user.mapper.UserRoleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【user_role(用户和角色表)】的数据库操作Service实现
* @createDate 2025-08-09 15:40:20
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 根据用户ID查询角色名称列表
     * @param userId 用户ID
     * @return 角色名称列表
     */
    @Override
    public List<Role> getRolesByUserId(Integer userId) {
        List<Role> roles = userRoleMapper.selectRolesByUserId(userId);
        if (roles.isEmpty()) {
            throw new BusinessException(UserResultCode.USER_ROLE_NOT_FOUND);
        }
        return roles;
    }

    /**
     * 根据用户ID列表查询角色ID列表
     * @param userIds 用户ID列表
     * @return 角色ID列表
     */
    @Override
    public List<Integer> getRoleIdsByUserIds(List<Integer> userIds) {
        return userRoleMapper.selectRoleIdsByUserIds(userIds);
    }

    /**
     * 为用户添加角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     */
    @Override
    public void addUserRoles(Integer userId, List<Integer> roleIds) {
        userRoleMapper.addUserRoles(userId, roleIds);
    }

    /**
     * 删除用户角色
     * @param userId 用户ID
     */
    @Override
    public void deleteUserRoles(Integer userId) {
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        remove(wrapper);
    }
}




