package com.thirty.user.service.domain.role.impl;

import com.thirty.common.utils.CollectionUtil;
import com.thirty.user.constant.RoleConstant;
import com.thirty.user.converter.RoleConverter;
import com.thirty.user.model.dto.RoleDTO;
import com.thirty.user.model.entity.Role;
import com.thirty.user.service.basic.RoleService;
import com.thirty.user.service.basic.RolePermissionService;
import com.thirty.user.service.basic.UserRoleService;
import com.thirty.user.service.basic.PermissionService;
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
    private RolePermissionService rolePermissionService;
    @Resource
    private PermissionService permissionService;

    /**
     * 添加角色
     * @param roleDTO 角色dto
     */
    @Override
    public void addRole(RoleDTO roleDTO) {
        Role role = RoleConverter.INSTANCE.toRole(roleDTO);
        roleService.addRole(role);
        // 获取全局角色有权限的页面
        List<Integer> globalViewIds = getPermissionIdsByParentRoleId(RoleConstant.GLOBAL_ROLE_PARENT_ID);
        List<Integer> parentViewIds = rolePermissionService.getPermissionIds(roleDTO.getParentNodeId());

        // 取公共部分
        List<Integer> commonViewIds = CollectionUtil.CommonCompare(globalViewIds, parentViewIds);

        // 分配权限权限
        rolePermissionService.addRolePermissions(role.getId(), commonViewIds);
    }

    /**
     * 添加全局角色
     * @param roleDTO 角色dto
     */
    @Override
    public void addGlobalRole(RoleDTO roleDTO) {
        Role role = RoleConverter.INSTANCE.toRole(roleDTO);
        roleService.addGlobalRole(role);
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
     * 更新全局角色
     * @param roleDTO 角色dto
     */
    @Override
    public void updateGlobalRole(RoleDTO roleDTO) {
        Role role = RoleConverter.INSTANCE.toRole(roleDTO);
        roleService.updateById(role);
    }

    /**
     * 删除角色
     * @param roleId 角色ID
     */
    @Override
    public void deleteRole(Integer roleId) {
        roleService.deleteRole(roleId);
        userRoleService.deleteByRoleId(roleId);
        rolePermissionService.deleteByRoleId(roleId);
    }

    /**
     * 分配权限权限
     * @param roleId 角色ID
     * @param oldPermissionIds 旧权限ID列表
     * @param newPermissionIds 新权限ID列表
     */
    @Override
    public void assignNormalPermission(Integer roleId, List<Integer> oldPermissionIds, List<Integer> newPermissionIds) {
        // 当前添加的权限以及当前添加的权限的祖宗权限
        List<Integer> addedAndAncestorViewIds = getAddedAndAncestorViewIds(oldPermissionIds, newPermissionIds);
        // 当前删除的权限以及当前删除的权限的子孙权限
        List<Integer> removedAndDescendantViewIds = getRemovedAndDescendantViewIds(oldPermissionIds, newPermissionIds);

        // 修改当前角色权限
        rolePermissionService.addRolePermissions(roleId, addedAndAncestorViewIds);
        rolePermissionService.deleteRolePermissions(roleId, removedAndDescendantViewIds);

        // 修改当前角色的所有子孙角色权限
        List<Integer> childRoleIds = roleService.getDescendantRoleIds(roleId);
        rolePermissionService.deleteRolePermissions(childRoleIds, removedAndDescendantViewIds);

        // 修改当前角色的所有祖先角色权限
        List<Integer> ancestorRoleIds = roleService.getAncestorRoleIds(roleId);
        rolePermissionService.addRolePermissions(ancestorRoleIds, addedAndAncestorViewIds);
    }

    /**
     * 分配全局权限权限
     * @param roleId 角色ID
     * @param oldPermissionIds 旧权限ID列表
     * @param newPermissionIds 新权限ID列表
     */
    @Override
    public void assignGlobalPermission(Integer roleId, List<Integer> oldPermissionIds, List<Integer> newPermissionIds) {
        // 当前添加的权限以及当前添加的权限的祖宗权限
        List<Integer> addedAndAncestorViewIds = getAddedAndAncestorViewIds(oldPermissionIds, newPermissionIds);
        // 当前删除的权限以及当前删除的权限的子孙权限
        List<Integer> removedAndDescendantViewIds = getRemovedAndDescendantViewIds(oldPermissionIds, newPermissionIds);

        // 修改当前角色权限
        rolePermissionService.addRolePermissions(roleId, addedAndAncestorViewIds);
        rolePermissionService.deleteRolePermissions(roleId, removedAndDescendantViewIds);

        // 修改当前所有普通角色的权限
        List<Integer> notGlobalRoleIds = roleService.getNotGlobalRoleIds();
        rolePermissionService.addRolePermissions(notGlobalRoleIds, addedAndAncestorViewIds);
    }

    /**
     * 获取角色权限ID列表
     * @param parentRoleId 父角色ID
     * @return 权限ID列表
     */
    @Override
    public List<Integer> getPermissionIdsByParentRoleId(Integer parentRoleId) {
        List<Integer> childRoleIds = roleService.getChildRoleIds(parentRoleId);
        return rolePermissionService.getPermissionIds(childRoleIds);
    }

    /**
     * 获取新增权限和祖先权限ID列表
     * @param oldViewIds 旧权限ID列表
     * @param newViewIds 新权限ID列表
     * @return 新增权限和祖先权限ID列表
     */
    private List<Integer> getAddedAndAncestorViewIds(List<Integer> oldViewIds, List<Integer> newViewIds) {
        List<Integer> addedViewIds = CollectionUtil.AddedCompare(oldViewIds, newViewIds);
        List<Integer> ancestorIds = permissionService.getAncestorIds(addedViewIds);

        // 流式去重
        return Stream.concat(addedViewIds.stream(), ancestorIds.stream()).distinct().collect(Collectors.toList());
    }

    /**
     * 获取减少权限和后代权限ID列表
     * @param oldViewIds 旧权限ID列表
     * @param newViewIds 新权限ID列表
     * @return 减少权限和后代权限ID列表
     */
    private List<Integer> getRemovedAndDescendantViewIds(List<Integer> oldViewIds, List<Integer> newViewIds) {
        List<Integer> removedViewIds = CollectionUtil.RemovedCompare(oldViewIds, newViewIds);
        List<Integer> descendantIds = permissionService.getDescendantIds(removedViewIds);

        // 流式去重
        return Stream.concat(removedViewIds.stream(), descendantIds.stream()).distinct().collect(Collectors.toList());
    }
}
