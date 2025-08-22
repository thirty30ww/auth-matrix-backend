package com.thirty.user.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.thirty.common.exception.BusinessException;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.entity.User;
import com.thirty.user.enums.result.UserResultCode;
import com.thirty.user.mapper.UserMapper;
import com.thirty.user.model.vo.UserVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserUtil {
    @Resource
    private UserMapper userMapper;

    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return 用户
     */
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(UserResultCode.USER_NOT_EXISTS);
        }
        return user;
    }

    /**
     * 从角色列表中提取出角色ID列表
     * @param roles 角色列表
     * @return 角色ID列表
     */
    public List<Integer> getRoleIdsFromRoles(List<Role> roles) {
        return roles.stream()
                .map(Role::getId)
                .collect(Collectors.toList());
    }

    /**
     * 验证用户列表中每个用户是否有指定的权限
     * @param permittedRoleIds 权限角色ID列表
     * @param userList 用户列表
     */
    public void setHasPermission(List<Integer> permittedRoleIds, IPage<UserVO> userList) {
        // 遍历每个UserVO，检查权限
        for (UserVO userVO : userList.getRecords()) {
            // 获取列表用户的角色
            List<Role> roles = userVO.getRoles();

            // 获取列表用户的角色ID
            List<Integer> userRoleIds = getRoleIdsFromRoles(roles);

            // 如果用户的所有角色ID都包含在权限角色列表中，则hasPermission为true
            boolean hasPermission = new HashSet<>(permittedRoleIds).containsAll(userRoleIds);
            userVO.setHasPermission(hasPermission);
        }
    }

    /**
     * 过滤出用户列表中有权限的用户
     * @param permittedRoleIds 权限角色ID列表
     * @param userList 用户列表
     */
    public void filterHasPermission(List<Integer> permittedRoleIds, IPage<UserVO> userList) {
        userList.getRecords().removeIf(userVO -> !userVO.getHasPermission());
    }
}
