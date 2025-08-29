package com.thirty.user.service.domain.role.impl;

import com.thirty.user.model.entity.Role;
import com.thirty.user.model.entity.User;
import com.thirty.user.service.basic.RoleService;
import com.thirty.user.service.basic.UserRoleService;
import com.thirty.user.service.basic.UserService;
import com.thirty.user.service.domain.role.RoleQueryDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleQueryDomainImpl implements RoleQueryDomain {
    @Resource
    private UserService userService;
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

    /**
     * 获取用户角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<Role> getUserRoles(Integer userId) {
        return userRoleService.getRolesByUserId(userId);
    }

    /**
     * 获取子角色列表
     * @param userId 用户ID
     * @return 子角色列表
     */
    @Override
    public List<Role> getChildRoles(Integer userId) {
        // 获取所有角色
        List<Role> allRoles = roleService.list();

        // 获取用户
        User user = userService.getById(userId);

        // 获取用户角色
        List<Integer> parentRoleIds = getRoleIds(user.getId());

        // 建立角色关系映射，key为父角色ID，value为子角色列表
        Map<Integer, List<Role>> parentChildMap = Role.buildParentChildMap(allRoles);

        // 递归收集所有子孙角色ID
        Set<Integer> childRoleIds = new HashSet<>();
        addChildRoleIds(parentRoleIds, parentChildMap, childRoleIds);

        // 返回子孙角色列表
        return allRoles.stream()
                .filter(role -> childRoleIds.contains(role.getId()))
                .collect(Collectors.toList());
    }

    /**
     * 获取全局角色列表
     * @return 全局角色列表
     */
    @Override
    public List<Role> getGlobalRoles() {
        List<Role> allRoles = roleService.list();
        return Role.getGlobalRoles(allRoles);
    }

    /**
     * 递归收集子角色ID
     * @param parentIds 父角色ID集合
     * @param parentChildMap 父子关系映射
     * @param resultIds 结果集合
     */
    private void addChildRoleIds(List<Integer> parentIds,
                                Map<Integer, List<Role>> parentChildMap,
                                Set<Integer> resultIds) {
        List<Integer> nextLevelIds = new ArrayList<>();

        for (Integer parentId : parentIds) {
            List<Role> children = parentChildMap.get(parentId);
            if (children != null) {
                for (Role child : children) {
                    if (!resultIds.contains(child.getId())) {
                        resultIds.add(child.getId());
                        nextLevelIds.add(child.getId());
                    }
                }
            }
        }

        // 如果还有下一层子角色，继续递归
        if (!nextLevelIds.isEmpty()) {
            addChildRoleIds(nextLevelIds, parentChildMap, resultIds);
        }
    }
}
