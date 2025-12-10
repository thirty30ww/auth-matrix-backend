// service/basic/factory/RolePermissionServiceFactory.java
package com.thirty.user.service.basic.factory;

import com.thirty.common.factory.BaseServiceFactory;
import com.thirty.user.model.entity.RolePermissionBk;
import com.thirty.user.model.entity.RolePermissionFt;
import com.thirty.user.model.entity.base.BaseRolePermission;
import com.thirty.user.service.basic.BaseRolePermissionService;
import com.thirty.user.service.basic.RolePermissionBkService;
import com.thirty.user.service.basic.RolePermissionFtService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class RolePermissionServiceFactory extends BaseServiceFactory<BaseRolePermissionService<?>, BaseRolePermission> {

    @Resource
    private RolePermissionBkService rolePermissionBkService;

    @Resource
    private RolePermissionFtService rolePermissionFtService;

    @PostConstruct
    public void init() {
        serviceMap = new HashMap<>();
        serviceMap.put(RolePermissionBk.class, rolePermissionBkService);
        serviceMap.put(RolePermissionFt.class, rolePermissionFtService);
    }
}
