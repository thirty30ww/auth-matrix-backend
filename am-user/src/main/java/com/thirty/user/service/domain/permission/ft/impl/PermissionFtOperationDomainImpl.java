package com.thirty.user.service.domain.permission.ft.impl;

import com.thirty.user.model.dto.PermissionFtDTO;
import com.thirty.user.model.entity.PermissionFt;
import com.thirty.user.model.entity.RolePermissionFt;
import com.thirty.user.service.domain.permission.base.impl.BasePermissionOperationDomainImpl;
import com.thirty.user.service.domain.permission.ft.PermissionFtOperationDomain;

public class PermissionFtOperationDomainImpl extends BasePermissionOperationDomainImpl<
        PermissionFt,
        RolePermissionFt,
        PermissionFtDTO
        >
        implements PermissionFtOperationDomain {
}
