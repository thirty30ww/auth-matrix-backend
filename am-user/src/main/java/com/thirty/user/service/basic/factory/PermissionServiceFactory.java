package com.thirty.user.service.basic.factory;

import com.thirty.common.factory.BaseServiceFactory;
import com.thirty.user.model.entity.PermissionBk;
import com.thirty.user.model.entity.PermissionFt;
import com.thirty.user.model.entity.base.BasePermission;
import com.thirty.user.service.basic.BasePermissionService;
import com.thirty.user.service.basic.PermissionBkService;
import com.thirty.user.service.basic.PermissionFtService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class PermissionServiceFactory extends BaseServiceFactory<BasePermissionService<?>, BasePermission> {
    @Resource
    private PermissionBkService permissionBkService;

    @Resource
    private PermissionFtService permissionFtService;

    @PostConstruct
    public void init() {
        serviceMap = new HashMap<>();
        serviceMap.put(PermissionBk.class, permissionBkService);
        serviceMap.put(PermissionFt.class, permissionFtService);
    }

}
