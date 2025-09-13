package com.thirty.user.service.domain.role.impl;

import com.thirty.common.utils.CollectionUtil;
import com.thirty.user.constant.RoleConstant;
import com.thirty.user.converter.RoleConverter;
import com.thirty.user.model.dto.RoleDTO;
import com.thirty.user.model.entity.Role;
import com.thirty.user.service.basic.RoleService;
import com.thirty.user.service.basic.RoleViewService;
import com.thirty.user.service.basic.UserRoleService;
import com.thirty.user.service.basic.ViewService;
import com.thirty.user.service.domain.role.RoleOperationDomain;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleOperationDomainImpl implements RoleOperationDomain {

    @Resource
    private RoleService roleService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private RoleViewService roleViewService;
    @Resource
    private ViewService viewService;

    /**
     * 添加角色
     * @param roleDTO 角色dto
     */
    @Override
    public void addRole(RoleDTO roleDTO) {
        Role role = RoleConverter.INSTANCE.toRole(roleDTO);
        roleService.addRole(role);
        // 获取全局角色有权限的页面
        List<Integer> globalViewIds = getViewIdsByParentRoleId(RoleConstant.GLOBAL_ROLE_PARENT_ID);
        List<Integer> parentViewIds = roleViewService.getViewIds(roleDTO.getParentNodeId());

        // 取公共部分
        List<Integer> commonViewIds = CollectionUtil.CommonCompare(globalViewIds, parentViewIds);

        // 分配视图权限
        roleViewService.addRoleViews(role.getId(), commonViewIds);
    }

    /**
     * 更新角色
     * @param roleDTO 角色dto
     */
    @Override
    public void updateRole(RoleDTO roleDTO) {
        Role role = RoleConverter.INSTANCE.toRole(roleDTO);
        roleService.updateRole(role);
    }

    /**
     * 删除角色
     * @param roleId 角色ID
     */
    @Override
    public void deleteRole(Integer roleId) {
        roleService.removeById(roleId);
        userRoleService.deleteByRoleId(roleId);
        roleViewService.deleteByRoleId(roleId);
    }


    /**
     * 分配视图权限
     * @param roleId 角色ID
     * @param oldViewIds 旧视图ID列表
     * @param newViewIds 新视图ID列表
     */
    @Override
    public void assignView(Integer roleId, List<Integer> oldViewIds, List<Integer> newViewIds) {
        List<Integer> addedAndAncestorViewIds = getAddedAndAncestorViewIds(oldViewIds, newViewIds);
        List<Integer> removedAndDescendantViewIds = getRemovedAndDescendantViewIds(oldViewIds, newViewIds);

        // 修改当前角色视图
        roleViewService.addRoleViews(roleId, addedAndAncestorViewIds);
        roleViewService.deleteRoleViews(roleId, removedAndDescendantViewIds);

        // 修改当前角色的所有子角色视图
        List<Integer> childRoleIds = roleService.getDescendantRoleIds(roleId);
        List<Integer> ancestorRoleIds = roleService.getAncestorRoleIds(roleId);
        roleViewService.addRoleViews(ancestorRoleIds, addedAndAncestorViewIds);
        roleViewService.deleteRoleViews(childRoleIds, removedAndDescendantViewIds);
    }

    /**
     * 获取角色视图ID列表
     * @param parentRoleId 父角色ID
     * @return 视图ID列表
     */
    @Override
    public List<Integer> getViewIdsByParentRoleId(Integer parentRoleId) {
        List<Integer> childRoleIds = roleService.getChildRoleIds(parentRoleId);
        return roleViewService.getViewIds(childRoleIds);
    }

    /**
     * 获取新增视图和祖先视图ID列表
     * @param oldViewIds 旧视图ID列表
     * @param newViewIds 新视图ID列表
     * @return 新增视图和祖先视图ID列表
     */
    private List<Integer> getAddedAndAncestorViewIds(List<Integer> oldViewIds, List<Integer> newViewIds) {
        List<Integer> addedViewIds = CollectionUtil.AddedCompare(oldViewIds, newViewIds);
        List<Integer> ancestorIds = viewService.getAncestorIds(addedViewIds);

        // 流式去重
        return Stream.concat(addedViewIds.stream(), ancestorIds.stream()).distinct().collect(Collectors.toList());
    }

    /**
     * 获取减少视图和后代视图ID列表
     * @param oldViewIds 旧视图ID列表
     * @param newViewIds 新视图ID列表
     * @return 减少视图和后代视图ID列表
     */
    private List<Integer> getRemovedAndDescendantViewIds(List<Integer> oldViewIds, List<Integer> newViewIds) {
        List<Integer> removedViewIds = CollectionUtil.RemovedCompare(oldViewIds, newViewIds);
        List<Integer> descendantIds = viewService.getDescendantIds(removedViewIds);

        // 流式去重
        return Stream.concat(removedViewIds.stream(), descendantIds.stream()).distinct().collect(Collectors.toList());
    }
}
