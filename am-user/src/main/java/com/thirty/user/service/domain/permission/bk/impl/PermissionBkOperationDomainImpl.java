package com.thirty.user.service.domain.permission.bk.impl;

import com.thirty.user.converter.PermissionBkConverter;
import com.thirty.user.model.dto.PermissionBkDTO;
import com.thirty.user.model.entity.PermissionBk;
import com.thirty.user.service.basic.RolePermissionBkService;
import com.thirty.user.service.basic.PermissionBkService;
import com.thirty.user.service.domain.permission.bk.PermissionBkOperationDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionBkOperationDomainImpl implements PermissionBkOperationDomain {

    @Resource
    private PermissionBkService permissionBkService;
    @Resource
    private RolePermissionBkService rolePermissionBkService;

    /**
     * 添加权限
     * @param permissionBkDTO 权限DTO
     */
    @Override
    public void addPermission(PermissionBkDTO permissionBkDTO) {
        PermissionBk permissionBk = PermissionBkConverter.INSTANCE.toPermission(permissionBkDTO);
        permissionBkService.tailInsert(permissionBk);
    }

    /**
     * 修改权限
     * @param permissionBkDTO 权限DTO
     */
    @Override
    public void modifyPermission(PermissionBkDTO permissionBkDTO) {
        PermissionBk permissionBk = PermissionBkConverter.INSTANCE.toPermission(permissionBkDTO);
        permissionBkService.modifyPermission(permissionBk);
    }

    /**
     * 删除权限
     * @param viewId 权限ID
     */
    @Override
    public void deletePermission(Integer viewId) {
        permissionBkService.deletePermission(viewId);
        rolePermissionBkService.deleteByPermissionId(viewId);
    }

    /**
     * 移动权限
     * @param viewId 权限ID
     * @param isUp 是否上移
     */
    @Override
    public void movePermission(Integer viewId, Boolean isUp) {
        if (isUp) {
            permissionBkService.moveUp(viewId);
        } else {
            permissionBkService.moveDown(viewId);
        }
    }
}
