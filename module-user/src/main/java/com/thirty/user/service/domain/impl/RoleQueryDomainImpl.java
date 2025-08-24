package com.thirty.user.service.domain.impl;

import com.thirty.user.model.entity.Role;
import com.thirty.user.model.entity.User;
import com.thirty.user.model.vo.RoleVO;
import com.thirty.user.service.basic.RoleService;
import com.thirty.user.service.basic.UserRoleService;
import com.thirty.user.service.basic.UserService;
import com.thirty.user.service.domain.RoleQueryDomain;
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
     * 获取角色列表
     * @param username 用户名
     * @return 角色列表
     */
    @Override
    public List<Role> getChildRoles(String username) {
        // 获取用户
        User user = userService.getUser(username);

        // 获取所有角色
        List<Role> allRoles = roleService.list();

        // 获取用户角色
        List<Role> userRoles = userRoleService.getRolesByUserId(user.getId());

        // 建立角色关系映射，key为父角色ID，value为子角色列表
        Map<Integer, List<Role>> parentChildMap = allRoles.stream()
                .filter(role -> role.getParentNodeId() != 0)
                .collect(Collectors.groupingBy(Role::getParentNodeId));

        // 用户角色的ID
        Set<Integer> parentRoleIds = userRoles.stream()
                .map(Role::getId)
                .collect(Collectors.toSet());

        // 递归收集所有子孙角色ID
        Set<Integer> childRoleIds = new HashSet<>();
        addChildRoleIds(parentRoleIds, parentChildMap, childRoleIds);

        // 添加全局子节点到childRoleIds
        addGlobalRoleIds(childRoleIds, allRoles);

        // 返回子孙角色列表
        return allRoles.stream()
                .filter(role -> childRoleIds.contains(role.getId()))
                .collect(Collectors.toList());
    }

    /**
     * 获取子角色ID列表
     * @param username 用户名
     * @return 子角色ID列表
     */
    @Override
    public List<Integer> getChildRoleIds(String username) {
        // 获取子角色列表
        List<Role> childRoles = getChildRoles(username);
        // 返回子角色ID列表
        return Role.extractIds(childRoles);
    }

    /**
     * 获取角色VO列表
     * @param username 用户名
     * @param hasPermissionDisplay 是否仅显示有权限操作的角色
     * @return 角色VO列表
     */
    @Override
    public List<RoleVO> getRoleTree(String username, boolean hasPermissionDisplay) {
        // 获取所有角色
        List<Role> allRoles = roleService.list();
        // 获取当前用户有权限的角色
        List<Role> permittedRoles = getChildRoles(username);

        Map<Integer, RoleVO> roleVOMap;
        // 是否仅显示有权限操作的角色
        if (hasPermissionDisplay) {
            roleVOMap = buildRoleVOMap(permittedRoles);
        } else {
            roleVOMap = buildRoleVOMap(allRoles, permittedRoles);
        }

        // 构建树形结构并返回根节点
        return buildTree(allRoles, roleVOMap);
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
     * 添加全局子节点到childRoleIds
     * @param childRoleIds 子角色ID集合
     * @param allRoles 所有角色列表
     */
    private void addGlobalRoleIds(Set<Integer> childRoleIds, List<Role> allRoles) {
        // 获取所有 parentNodeId = -1 的全局子节点
        List<Role> globalChildRoles = Role.getGlobalRoles(allRoles);

        // 添加全局子节点（parentNodeId = -1 的节点是所有节点的下级）
        for (Role globalChild : globalChildRoles) {
            childRoleIds.add(globalChild.getId());
        }
    }

    /**
     * 递归收集子角色ID
     * @param parentIds 父角色ID集合
     * @param parentChildMap 父子关系映射
     * @param resultIds 结果集合
     */
    private void addChildRoleIds(Set<Integer> parentIds,
                                Map<Integer, List<Role>> parentChildMap,
                                Set<Integer> resultIds) {
        Set<Integer> nextLevelIds = new HashSet<>();

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

    /**
     * 构建RoleVO节点映射
     * @param allRoles 角色列表
     * @param permittedRoles 用户有权限的角色列表
     * @return RoleVO节点映射
     */
    private Map<Integer, RoleVO> buildRoleVOMap(List<Role> allRoles, List<Role> permittedRoles) {
        // 创建用户权限映射
        Set<Integer> permittedRoleIds = permittedRoles.stream()
                .map(Role::getId)
                .collect(Collectors.toSet());

        Map<Integer, RoleVO> roleVOMap = new HashMap<>();

        // 创建并有权限的角色的节点映射
        allRoles.forEach(role -> {
            RoleVO roleVO = new RoleVO();
            roleVO.setNode(role);
            roleVO.setHasPermission(permittedRoleIds.contains(role.getId()));
            roleVO.setChildren(new ArrayList<>());
            roleVOMap.put(role.getId(), roleVO);
        });

        return roleVOMap;
    }

    /**
     * 构建RoleVO节点映射
     * @param allRoles 角色列表
     * @return RoleVO节点映射
     */
    private Map<Integer, RoleVO> buildRoleVOMap(List<Role> allRoles) {
        Map<Integer, RoleVO> roleVOMap = new HashMap<>();
        allRoles.forEach(role -> {
            RoleVO roleVO = new RoleVO();
            roleVO.setNode(role);
            roleVO.setHasPermission(true);
            roleVO.setChildren(new ArrayList<>());
            roleVOMap.put(role.getId(), roleVO);
        });
        return roleVOMap;
    }

    /**
     * 构建树形结构
     * @param roles 角色列表
     * @param roleVOMap RoleVO节点映射
     * @return 根节点列表
     */
    private List<RoleVO> buildTree(List<Role> roles, Map<Integer, RoleVO> roleVOMap) {
        List<RoleVO> rootNodes = new ArrayList<>();

        // 构建树形结构
        for (Role role : roles) {
            RoleVO currentRoleVO = roleVOMap.get(role.getId());

            if (role.getParentNodeId() == 0) {
                // 根节点
                rootNodes.add(currentRoleVO);
            } else {
                // 子节点，添加到父节点的children中
                RoleVO parentRoleVO = roleVOMap.get(role.getParentNodeId());
                if (parentRoleVO != null) {
                    parentRoleVO.getChildren().add(currentRoleVO);
                }
            }
        }

        return rootNodes;
    }
}
