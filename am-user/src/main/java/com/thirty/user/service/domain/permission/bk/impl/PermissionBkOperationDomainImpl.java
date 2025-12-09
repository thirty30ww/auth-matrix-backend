package com.thirty.user.service.domain.permission.bk.impl;

import com.thirty.user.model.dto.PermissionBkDTO;
import com.thirty.user.model.entity.PermissionBk;
import com.thirty.user.model.entity.RolePermissionBk;
import com.thirty.user.service.basic.PermissionBkService;
import com.thirty.user.service.basic.RolePermissionBkService;
import com.thirty.user.service.domain.permission.base.impl.BasePermissionOperationDomainImpl;
import com.thirty.user.service.domain.permission.bk.PermissionBkOperationDomain;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionBkOperationDomainImpl extends BasePermissionOperationDomainImpl<
        PermissionBk,
        PermissionBkDTO,
        RolePermissionBk
        > implements PermissionBkOperationDomain {

    /**
     * 构造函数
     * @param permissionBkService 后台权限服务
     * @param rolePermissionBkService 角色后台权限服务
     */
    public PermissionBkOperationDomainImpl(
            PermissionBkService permissionBkService,
            RolePermissionBkService rolePermissionBkService
    ) {
        super(permissionBkService, rolePermissionBkService);
    }
}
