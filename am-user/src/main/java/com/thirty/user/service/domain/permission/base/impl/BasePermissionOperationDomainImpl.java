package com.thirty.user.service.domain.permission.base.impl;

import com.thirty.user.converter.PermissionGenericConverter;
import com.thirty.user.model.dto.base.BasePermissionDTO;
import com.thirty.user.model.entity.base.BasePermission;
import com.thirty.user.model.entity.base.BaseRolePermission;
import com.thirty.user.service.basic.BasePermissionService;
import com.thirty.user.service.basic.BaseRolePermissionService;
import com.thirty.user.service.domain.permission.base.BasePermissionOperationDomain;
import jakarta.annotation.Resource;

public class BasePermissionOperationDomainImpl<
        T extends BasePermission,
        DTO extends BasePermissionDTO,
        RP extends BaseRolePermission
        >
        implements BasePermissionOperationDomain<DTO> {

    @Resource
    private PermissionGenericConverter permissionGenericConverter;

    protected final BasePermissionService<T> permissionService;
    protected final BaseRolePermissionService<RP> rolePermissionService;

    /**
     * 构造函数
     * @param permissionService 权限服务
     * @param rolePermissionService 角色权限服务
     */
    public BasePermissionOperationDomainImpl(
            BasePermissionService<T> permissionService,
            BaseRolePermissionService<RP> rolePermissionService) {
        this.permissionService = permissionService;
        this.rolePermissionService = rolePermissionService;
    }

    /**
     * 添加权限
     * @param permissionDTO 权限DTO对象
     */
    @Override
    public void addPermission(DTO permissionDTO) {
        T permission = permissionGenericConverter.toPermission(permissionDTO);
        permissionService.tailInsert(permission);
    }

    /**
     * 修改权限
     * @param permissionDTO 权限DTO对象
     */
    @Override
    public void modifyPermission(DTO permissionDTO) {
        T permission = permissionGenericConverter.toPermission(permissionDTO);
        permissionService.modifyPermission(permission);
    }

    /**
     * 删除权限
     * @param permissionId 权限ID
     */
    @Override
    public void deletePermission(Integer permissionId) {
        permissionService.deletePermission(permissionId);
        rolePermissionService.deleteByPermissionId(permissionId);
    }

    /**
     * 移动权限
     * @param permissionId 权限ID
     * @param isUp 是否向上移动
     */
    @Override
    public void movePermission(Integer permissionId, Boolean isUp) {
        if (isUp) {
            permissionService.moveUp(permissionId);
        } else {
            permissionService.moveDown(permissionId);
        }
    }
}
