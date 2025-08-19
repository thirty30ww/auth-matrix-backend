package com.thirty.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.user.model.entity.Role;
import com.thirty.user.service.RoleService;
import com.thirty.user.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author Lenovo
* @description 针对表【role(角色表)】的数据库操作Service实现
* @createDate 2025-08-09 15:40:41
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    /**
     * 获取子角色列表
     * @param roles 当前用户的角色列表
     * @return 子角色列表
     */
    @Override
    public List<Role> getChildRoleList(List<Role> roles) {
        // 获取所有角色数据，建立角色关系映射
         List<Role> allRoles = list();
        Map<Integer, List<Role>> parentChildMap = allRoles.stream()
                .filter(role -> role.getParentNodeId() != 0)
                .collect(Collectors.groupingBy(Role::getParentNodeId));
        
        // 所有已有角色的ID
        Set<Integer> parentRoleIds = roles.stream()
                .map(Role::getId)
                .collect(Collectors.toSet());

        // 递归收集所有子孙角色ID
        Set<Integer> childRoleIds = new HashSet<>();
        getChildRoleIds(parentRoleIds, parentChildMap, childRoleIds);

        // 添加全局子节点到childRoleIds
        addGlobalChildIds(childRoleIds, allRoles);
        
        // 返回子孙角色列表
        return allRoles.stream()
                .filter(role -> childRoleIds.contains(role.getId()))
                .collect(Collectors.toList());
    }

    /**
     * 添加全局子节点到childRoleIds
     * @param childRoleIds 子角色ID集合
     * @param allRoles 所有角色列表
     */
    private void addGlobalChildIds(Set<Integer> childRoleIds, List<Role> allRoles) {
        // 获取所有 parentNodeId = -1 的全局子节点
        List<Role> globalChildRoles = allRoles.stream()
                .filter(role -> role.getParentNodeId() == -1)
                .toList();

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
    private void getChildRoleIds(Set<Integer> parentIds,
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
            getChildRoleIds(nextLevelIds, parentChildMap, resultIds);
        }
    }
}




