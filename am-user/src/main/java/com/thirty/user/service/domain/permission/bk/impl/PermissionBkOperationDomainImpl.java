package com.thirty.user.service.domain.permission.bk.impl;

import com.thirty.user.model.dto.PermissionBkDTO;
import com.thirty.user.model.entity.PermissionBk;
import com.thirty.user.model.entity.RolePermissionBk;
import com.thirty.user.service.domain.permission.base.impl.BasePermissionOperationDomainImpl;
import com.thirty.user.service.domain.permission.bk.PermissionBkOperationDomain;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionBkOperationDomainImpl extends BasePermissionOperationDomainImpl<
        PermissionBk,
        RolePermissionBk,
        PermissionBkDTO
        > implements PermissionBkOperationDomain {
}
