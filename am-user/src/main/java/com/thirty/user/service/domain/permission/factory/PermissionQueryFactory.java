package com.thirty.user.service.domain.permission.factory;

import com.thirty.common.factory.BaseServiceFactory;
import com.thirty.user.model.entity.PermissionBk;
import com.thirty.user.model.entity.PermissionFt;
import com.thirty.user.model.entity.base.BasePermission;
import com.thirty.user.service.domain.permission.base.BasePermissionQueryDomain;
import com.thirty.user.service.domain.permission.bk.PermissionBkQueryDomain;
import com.thirty.user.service.domain.permission.ft.PermissionFtQueryDomain;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class PermissionQueryFactory extends BaseServiceFactory<BasePermissionQueryDomain, BasePermission> {
    @Resource
    private PermissionBkQueryDomain permissionBkQueryDomain;

    @Resource
    private PermissionFtQueryDomain permissionFtQueryDomain;

    @PostConstruct
    public void init() {
        registerService(PermissionBk.class, permissionBkQueryDomain);
        registerService(PermissionFt.class, permissionFtQueryDomain);
    }
}
