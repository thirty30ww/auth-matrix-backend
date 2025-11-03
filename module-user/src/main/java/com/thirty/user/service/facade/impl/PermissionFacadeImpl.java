package com.thirty.user.service.facade.impl;

import com.thirty.common.exception.BusinessException;
import com.thirty.user.constant.PermissionConstant;
import com.thirty.user.enums.model.PermissionType;
import com.thirty.user.enums.model.RoleType;
import com.thirty.user.enums.result.PermissionResultCode;
import com.thirty.user.model.dto.PermissionDTO;
import com.thirty.user.model.vo.PermissionVO;
import com.thirty.user.service.domain.permission.PermissionOperationDomain;
import com.thirty.user.service.domain.permission.PermissionQueryDomain;
import com.thirty.user.service.domain.permission.PermissionValidationDomain;
import com.thirty.user.service.domain.permission.builder.PermissionsBuilderFactory;
import com.thirty.user.service.domain.role.RoleQueryDomain;
import com.thirty.user.service.domain.role.builder.RolesBuilderFactory;
import com.thirty.user.service.facade.PermissionFacade;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PermissionFacadeImpl implements PermissionFacade {
    @Resource
    private PermissionQueryDomain permissionQueryDomain;
    @Resource
    private PermissionValidationDomain permissionValidationDomain;
    @Resource
    private PermissionOperationDomain permissionOperationDomain;
    @Resource
    private RoleQueryDomain roleQueryDomain;

    @Resource
    private RolesBuilderFactory rolesBuilderFactory;
    @Resource
    private PermissionsBuilderFactory permissionsBuilderFactory;

    /**
     * 获取权限树
     * @param userId 用户ID
     * @return 权限树
     */
    @Override
    public List<PermissionVO> getViewTree(Integer userId) {
        List<Integer> currentRoleIds = roleQueryDomain.getRoleIds(userId);
        return permissionsBuilderFactory.create(currentRoleIds)
                .withPermissionTypes(List.of(PermissionType.DIRECTORY, PermissionType.MENU, PermissionType.PAGE))
                .withPermissionFlag()
                .buildTree();
    }

    /**
     * 获取菜单树
     * @param userId 用户ID
     * @return 菜单树
     */
    @Override
    public List<PermissionVO> getMenuTree(Integer userId) {
        List<Integer> currentRoleIds = roleQueryDomain.getRoleIds(userId);
        return permissionsBuilderFactory.create(currentRoleIds)
                .withPermissionTypes(List.of(PermissionType.DIRECTORY, PermissionType.MENU))
                .filterByPermission()
                .buildTree();
    }

    /**
     * 获取菜单和按钮树
     * @param userId 用户ID
     * @return 菜单和按钮树
     */
    @Override
    public List<PermissionVO> getMenuAndButtonTree(Integer userId, Integer targetRoleId) {
        List<Integer> currentRoleIds = roleQueryDomain.getRoleIds(userId);

        return permissionsBuilderFactory.create(currentRoleIds, targetRoleId)
                .withPermissionTypes(List.of(PermissionType.DIRECTORY, PermissionType.MENU, PermissionType.BUTTON))
                .withChangeFlag()
                .withPermissionFlag()
                .buildTree();
    }
    /**
     * 获取目录树
     * @param userId 用户ID
     * @return 目录树
     */
    @Override
    public List<PermissionVO> getDirectoryTree(Integer userId) {
        List<Integer> currentRoleIds = roleQueryDomain.getRoleIds(userId);
        return permissionsBuilderFactory.create(currentRoleIds)
                .withPermissionTypes(List.of(PermissionType.DIRECTORY))
                .filterByPermission()
                .buildTree();
    }

    /**
     * 获取权限列表
     * @param keyword 权限名称关键词
     * @return 权限列表
     */
    @Override
    public List<PermissionVO> getViewVOS(Integer userId, String keyword) {
        List<Integer> currentRoleIds = roleQueryDomain.getRoleIds(userId);
        return permissionsBuilderFactory.create(currentRoleIds)
                .withPermissionTypes(List.of(PermissionType.DIRECTORY, PermissionType.MENU, PermissionType.PAGE))
                .withKeyword(keyword)
                .filterByPermission()
                .build();
    }

    /**
     * 获取权限码列表
     * @param userId 用户ID
     * @return 权限码列表
     */
    @Override
    public List<String> getPermissionCode(Integer userId) {
        List<Integer> currentAndChildRoleIds = rolesBuilderFactory.create(userId).forRoleTypes(List.of(
                RoleType.SELF, RoleType.CHILD
        )).buildIds();
        return permissionQueryDomain.getPermissionCode(currentAndChildRoleIds);
    }

    /**
     * 添加权限
     * @param userId 用户ID
     * @param permissionDTO 权限DTO
     */
    @Override
    public void addPermission(Integer userId, PermissionDTO permissionDTO) {
        if (!permissionValidationDomain.validateTypeComply(permissionDTO.getParentNodeId(), permissionDTO.getType())) {
            throw new BusinessException(PermissionResultCode.PERMISSION_TYPE_NOT_COMPLY);
        }
        if (!Objects.equals(permissionDTO.getParentNodeId(), PermissionConstant.ROOT_PERMISSION_PARENT_ID)
                && !permissionValidationDomain.validateUserHavePermission(userId, permissionDTO.getParentNodeId())) {
            throw new BusinessException(PermissionResultCode.PERMISSION_NOT_AUTHORIZED_ADD);
        }
        permissionOperationDomain.addPermission(permissionDTO);
    }

    /**
     * 修改权限
     * @param userId 用户ID
     * @param permissionDTO 权限DTO
     */
    @Override
    public void modifyPermission(Integer userId, PermissionDTO permissionDTO) {
        // 父ID不能等于当前ID或者当前ID的子ID
        if (permissionValidationDomain.validateParentIdEqualsSelfAndDescendants(permissionDTO.getId(), permissionDTO.getParentNodeId())) {
            throw new BusinessException(PermissionResultCode.PERMISSION_CANNOT_BE_PARENT);
        }
        // 父权限有规定的子权限类型，比如 DIRECTORY 的子权限不能是 BUTTON
        if (!permissionValidationDomain.validateTypeComply(permissionDTO.getParentNodeId(), permissionDTO.getType())) {
            throw new BusinessException(PermissionResultCode.PERMISSION_TYPE_NOT_COMPLY);
        }
        if (!permissionValidationDomain.validateModifyValid(userId, permissionDTO.getId(), permissionDTO.getIsValid())) {
            throw new BusinessException(PermissionResultCode.PERMISSION_CANNOT_MODIFY_VALID);
        }
        if (!permissionValidationDomain.validateUserHavePermission(userId, permissionDTO.getId())) {
            throw new BusinessException(PermissionResultCode.PERMISSION_NOT_AUTHORIZED_MODIFY);
        }
        permissionOperationDomain.modifyPermission(permissionDTO);
    }

    /**
     * 删除权限
     * @param userId 用户ID
     * @param viewId 权限ID
     */
    @Override
    public void deletePermission(Integer userId, Integer viewId) {
        if (!permissionValidationDomain.validateUserHavePermission(userId, viewId)) {
            throw new BusinessException(PermissionResultCode.PERMISSION_NOT_AUTHORIZED_DELETE);
        }
        permissionOperationDomain.deletePermission(viewId);
    }
    /**
     * 移动权限
     * @param viewId 权限ID
     * @param isUp 是否上移
     */
    @Override
    public void movePermission(Integer viewId, Boolean isUp) {
        if (isUp && !permissionValidationDomain.validateMoveUp(viewId)) {
            throw new BusinessException(PermissionResultCode.PERMISSION_CANNOT_MOVE_UP);
        }
        if (!isUp && !permissionValidationDomain.validateMoveDown(viewId)) {
            throw new BusinessException(PermissionResultCode.PERMISSION_CANNOT_MOVE_DOWN);
        }
        permissionOperationDomain.movePermission(viewId, isUp);
    }

}
