package com.thirty.user.service.domain.permission.bk.impl;

import com.thirty.user.enums.model.PermissionBkType;
import com.thirty.user.model.entity.PermissionBk;
import com.thirty.user.model.entity.RolePermissionBk;
import com.thirty.user.service.basic.PermissionBkService;
import com.thirty.user.service.domain.permission.base.impl.BasePermissionQueryDomainImpl;
import com.thirty.user.service.domain.permission.bk.PermissionBkQueryDomain;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionBkQueryDomainImpl extends BasePermissionQueryDomainImpl<PermissionBk, RolePermissionBk>
        implements PermissionBkQueryDomain {

    private final PermissionBkService permissionBkService;

    public PermissionBkQueryDomainImpl(PermissionBkService permissionBkService) {
        this.permissionBkService = permissionBkService;
    }

    @Override
    protected List<Integer> getAllPermissionIds() {
        return PermissionBk.extractPermissionIds(permissionBkService.getPermissionByTypes(List.of(
                PermissionBkType.DIRECTORY,
                PermissionBkType.MENU,
                PermissionBkType.BUTTON
        )));
    }
}
