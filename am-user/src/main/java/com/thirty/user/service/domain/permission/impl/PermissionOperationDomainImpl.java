package com.thirty.user.service.domain.permission.impl;

import com.thirty.user.converter.PermissionConverter;
import com.thirty.user.model.dto.PermissionDTO;
import com.thirty.user.model.entity.Permission;
import com.thirty.user.service.basic.RolePermissionService;
import com.thirty.user.service.basic.PermissionService;
import com.thirty.user.service.domain.permission.PermissionOperationDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionOperationDomainImpl implements PermissionOperationDomain {

    @Resource
    private PermissionService permissionService;
    @Resource
    private RolePermissionService rolePermissionService;

    /**
     * 添加权限
     * @param permissionDTO 权限DTO
     */
    @Override
    public void addPermission(PermissionDTO permissionDTO) {
        Permission permission = PermissionConverter.INSTANCE.toPermission(permissionDTO);
        permissionService.tailInsert(permission);
    }

    /**
     * 修改权限
     * @param permissionDTO 权限DTO
     */
    @Override
    public void modifyPermission(PermissionDTO permissionDTO) {
        Permission permission = PermissionConverter.INSTANCE.toPermission(permissionDTO);
        permissionService.modifyPermission(permission);
    }

    /**
     * 删除权限
     * @param viewId 权限ID
     */
    @Override
    public void deletePermission(Integer viewId) {
        permissionService.deletePermission(viewId);
        rolePermissionService.deleteByPermissionId(viewId);
    }

    /**
     * 移动权限
     * @param viewId 权限ID
     * @param isUp 是否上移
     */
    @Override
    public void movePermission(Integer viewId, Boolean isUp) {
        if (isUp) {
            permissionService.moveUp(viewId);
        } else {
            permissionService.moveDown(viewId);
        }
    }
}
