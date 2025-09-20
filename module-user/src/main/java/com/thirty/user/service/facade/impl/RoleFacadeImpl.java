package com.thirty.user.service.facade.impl;

import com.thirty.common.exception.BusinessException;
import com.thirty.system.api.SettingApi;
import com.thirty.user.enums.model.RoleType;
import com.thirty.user.enums.model.RolesType;
import com.thirty.user.enums.result.PermissionResultCode;
import com.thirty.user.enums.result.RoleResultCode;
import com.thirty.user.model.dto.AssignViewDTO;
import com.thirty.user.model.dto.RoleDTO;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.vo.RoleVO;
import com.thirty.user.service.domain.permission.PermissionQueryDomain;
import com.thirty.user.service.domain.permission.PermissionValidationDomain;
import com.thirty.user.service.domain.role.RoleOperationDomain;
import com.thirty.user.service.domain.role.builder.RoleValidationBuilderFactory;
import com.thirty.user.service.domain.role.builder.RolesBuilderFactory;
import com.thirty.user.service.facade.RoleFacade;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleFacadeImpl implements RoleFacade {

    @Resource
    private RoleOperationDomain roleOperationDomain;
    @Resource
    private PermissionQueryDomain permissionQueryDomain;
    @Resource
    private PermissionValidationDomain permissionValidationDomain;

    @Resource
    private RolesBuilderFactory rolesBuilderFactory;
    @Resource
    private RoleValidationBuilderFactory roleValidationBuilderFactory;

    @Resource
    private SettingApi settingApi;

    /**
     * 获取角色树
     * @param currentUserId 当前用户ID
     * @param type 类型
     * @return 角色树
     */
    @Override
    public List<RoleVO> getRoleTree(Integer currentUserId, RolesType type) {
        List<RoleType> roleTypes = type.toRoleTypes();
        List<RoleVO> roleVOS;
        if (type == RolesType.NOT_GLOBAL) {
            // 当前用户的子角色和全局角色是有权限的角色
            List<Integer> permittedRoleIds = rolesBuilderFactory
                    .create(currentUserId)
                    .forRoleTypes(List.of(RoleType.CHILD, RoleType.GLOBAL))
                    .buildIds();
            // 获取非全局的所有角色，并标注好hasPermission（是否有权限）
            roleVOS = rolesBuilderFactory.create().forRoleType(RoleType.NOT_GLOBAL)
                    .buildTree(settingApi.hasPermissionDisplay(), permittedRoleIds);
        } else {
            roleVOS = rolesBuilderFactory.create(currentUserId).forRoleTypes(roleTypes).buildTree();
        }
        return roleVOS;
    }

    /**
     * 获取角色列表
     * @param currentUserId 当前用户ID
     * @param type 类型
     * @return 角色列表
     */
    @Override
    public List<Role> getRoles(Integer currentUserId, RolesType type) {
        List<RoleType> roleTypes = type.toRoleTypes();
        return rolesBuilderFactory.create(currentUserId).forRoleTypes(roleTypes).build();
    }

    /**
     * 添加角色
     * @param roleDTO 角色dto
     */
    @Override
    public void addRole(RoleDTO roleDTO, Integer userId) {
        // 如果要添加的角色的父角色不是当前角色的子角色或者本身，则不能添加
        if (!roleValidationBuilderFactory.create(userId)
                .forRoleTypes(RolesType.CHILD_AND_SELF.toRoleTypes())
                .validateRole(roleDTO.getParentNodeId())
        ) {
            throw new BusinessException(RoleResultCode.ROLE_NOT_AUTHORIZED_ADD);
        }
        roleOperationDomain.addRole(roleDTO);
    }

    /**
     * 修改角色
     * @param roleDTO 角色dto
     * @param userId 用户ID
     */
    @Override
    public void updateRole(RoleDTO roleDTO, Integer userId) {
        // 如果要修改的角色不是当前角色的子角色，则不能修改
        if (!roleValidationBuilderFactory.create(userId)
                .forRoleType(RoleType.CHILD)
                .validateRole(roleDTO.getId())
        ) {
            throw new BusinessException(RoleResultCode.ROLE_NOT_AUTHORIZED_UPDATE);
        }
        // 如果要修改角色的父角色不是当前角色的子角色或者本身，则不能修改
        if (!roleValidationBuilderFactory.create(userId)
                .forRoleTypes(RolesType.CHILD_AND_SELF.toRoleTypes())
                .validateRole(roleDTO.getParentNodeId())
        ) {
            throw new BusinessException(RoleResultCode.ROLE_NOT_AUTHORIZED_UPDATE);
        }
        roleOperationDomain.updateRole(roleDTO);
    }

    /**
     * 删除角色
     * @param roleId 角色ID
     * @param userId 用户ID
     */
    @Override
    public void deleteRole(Integer roleId, Integer userId) {
        // 如果要删除的角色不是当前角色的子角色，则不能删除
        if (!roleValidationBuilderFactory.create(userId)
                .forRoleType(RoleType.CHILD)
                .validateRole(roleId)
        ) {
            throw new BusinessException(RoleResultCode.ROLE_NOT_AUTHORIZED_DELETE);
        }
        roleOperationDomain.deleteRole(roleId);
    }

    /**
     * 分配视图权限
     * @param userId 当前操作用户ID
     * @param assignViewDTO 分配视图dto
     */
    @Override
    public void assignView(Integer userId, AssignViewDTO assignViewDTO) {
        Integer targetRoleId = assignViewDTO.getRoleId();
        List<Integer> newViewIds = assignViewDTO.getViewIds();

        // 如果要分配权限的角色不是当前角色的子角色，则不能分配
        if (!roleValidationBuilderFactory.create(userId)
                .forRoleType(RoleType.CHILD)
                .validateRole(targetRoleId)
        ) {
            throw new BusinessException(RoleResultCode.ROLE_NOT_AUTHORIZED_ASSIGN);
        }
        // 如果要分配的权限是当前角色没有的，则不能分配
        if (!permissionValidationDomain.validateViewContainUserViews(userId, newViewIds)) {
            throw new BusinessException(PermissionResultCode.VIEW_NOT_AUTHORIZED_ASSIGN);
        }

        List<Integer> oldViewIds = permissionQueryDomain.getPermissionId(targetRoleId);
        roleOperationDomain.assignView(targetRoleId, oldViewIds, newViewIds);
    }
}
