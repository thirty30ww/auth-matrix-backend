package com.thirty.user.service.domain.permission.ft.impl;

import com.thirty.user.model.entity.PermissionFt;
import com.thirty.user.model.entity.RolePermissionFt;
import com.thirty.user.service.domain.permission.base.impl.BasePermissionQueryDomainImpl;
import com.thirty.user.service.domain.permission.ft.PermissionFtQueryDomain;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionFtQueryDomainImpl extends BasePermissionQueryDomainImpl<
        PermissionFt,
        RolePermissionFt
        >
        implements PermissionFtQueryDomain {

    @Override
    protected List<Integer> getAllPermissionIds() {
        return List.of(1, 2, 3);
    }
}
