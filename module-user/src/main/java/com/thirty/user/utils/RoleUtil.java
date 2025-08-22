package com.thirty.user.utils;

import com.thirty.user.model.entity.Role;
import com.thirty.user.model.vo.RoleVO;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RoleUtil {
    /**
     * 获取子角色列表
     * @param roles 当前用户的角色列表
     * @return 子角色列表
     */
    public List<Role> getChildRoleList(List<Role> roles, List<Role> allRoles) {
        // 获取所有角色数据，建立角色关系映射
        Map<Integer, List<Role>> parentChildMap = allRoles.stream()
                .filter(role -> role.getParentNodeId() != 0)
                .collect(Collectors.groupingBy(Role::getParentNodeId));

        // 所有已有角色的ID
        Set<Integer> parentRoleIds = roles.stream()
                .map(Role::getId)
                .collect(Collectors.toSet());

        // 递归收集所有子孙角色ID
        Set<Integer> childRoleIds = new HashSet<>();
        addChildRoleIds(parentRoleIds, parentChildMap, childRoleIds);

        // 添加全局子节点到childRoleIds
        addGlobalChildIds(childRoleIds, allRoles);

        // 返回子孙角色列表
        return allRoles.stream()
                .filter(role -> childRoleIds.contains(role.getId()))
                .collect(Collectors.toList());
    }

    /**
     * 获取全局子角色列表
     * @param allRoles 所有角色列表
     * @return 全局子角色列表
     */
    public List<Role> getGlobalChildRoles(List<Role> allRoles) {
        // 获取所有 parentNodeId = -1 的全局子节点
        return allRoles.stream()
                .filter(role -> role.getParentNodeId() == -1)
                .toList();
    }

    /**
     * 添加全局子节点到childRoleIds
     * @param childRoleIds 子角色ID集合
     * @param allRoles 所有角色列表
     */
    public void addGlobalChildIds(Set<Integer> childRoleIds, List<Role> allRoles) {
        // 获取所有 parentNodeId = -1 的全局子节点
        List<Role> globalChildRoles = getGlobalChildRoles(allRoles);

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
    public void addChildRoleIds(Set<Integer> parentIds,
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
    public Map<Integer, RoleVO> buildRoleVOMap(List<Role> allRoles, List<Role> permittedRoles) {
        // 创建用户权限映射
        Set<Integer> permittedRoleIds = permittedRoles.stream()
                .map(Role::getId)
                .collect(Collectors.toSet());

        Map<Integer, RoleVO> roleVOMap = new HashMap<>();

        // 创建所有的RoleVO节点
        for (Role role : allRoles) {
            RoleVO roleVO = new RoleVO();
            roleVO.setNode(role);
            roleVO.setHasPermission(permittedRoleIds.contains(role.getId()));
            roleVO.setChildren(new ArrayList<>());
            roleVOMap.put(role.getId(), roleVO);
        }

        return roleVOMap;
    }

    /**
     * 构建RoleVO节点映射
     * @param allRoles 角色列表
     * @return RoleVO节点映射
     */
    public Map<Integer, RoleVO> buildRoleVOMap(List<Role> allRoles) {
        Map<Integer, RoleVO> roleVOMap = new HashMap<>();
        for (Role role : allRoles) {
            RoleVO roleVO = new RoleVO();
            roleVO.setNode(role);
            roleVO.setHasPermission(true);
            roleVO.setChildren(new ArrayList<>());
            roleVOMap.put(role.getId(), roleVO);
        }
        return roleVOMap;
    }

    /**
     * 构建树形结构
     * @param roles 角色列表
     * @param roleVOMap RoleVO节点映射
     * @return 根节点列表
     */
    public List<RoleVO> buildTree(List<Role> roles, Map<Integer, RoleVO> roleVOMap) {
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
