package com.thirty.user.service.domain.permission.impl;

import com.thirty.user.converter.PermissionConverter;
import com.thirty.user.model.dto.PermissionDTO;
import com.thirty.user.model.entity.Permission;
import com.thirty.user.service.basic.RoleViewService;
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
    private RoleViewService roleViewService;

    /**
     * 添加视图
     * @param permissionDTO 视图DTO
     */
    @Override
    public void addPermission(PermissionDTO permissionDTO) {
        Permission permission = PermissionConverter.INSTANCE.toPermission(permissionDTO);
        permissionService.tailInsert(permission);
    }

    /**
     * 修改视图
     * @param permissionDTO 视图DTO
     */
    @Override
    public void modifyPermission(PermissionDTO permissionDTO) {
        Permission permission = PermissionConverter.INSTANCE.toPermission(permissionDTO);
        permissionService.modifyPermission(permission);
    }

    /**
     * 删除视图
     * @param viewId 视图ID
     */
    @Override
    public void deletePermission(Integer viewId) {
        permissionService.deletePermission(viewId);
        roleViewService.deleteByViewId(viewId);
    }

    /**
     * 移动视图
     * @param viewId 视图ID
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
