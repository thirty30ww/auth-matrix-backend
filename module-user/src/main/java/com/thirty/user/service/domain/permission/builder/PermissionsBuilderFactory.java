package com.thirty.user.service.domain.permission.builder;

import com.thirty.user.service.basic.PermissionService;
import com.thirty.user.service.domain.permission.PermissionQueryDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionsBuilderFactory {
    @Resource
    private PermissionService permissionService;
    @Resource
    private PermissionQueryDomain permissionQueryDomain;

    public PermissionsBuilder create() {
        return new PermissionsBuilder(permissionService, permissionQueryDomain);
    }

    public PermissionsBuilder create(List<Integer> currentAndChildRoleIds) {
        return create().forRoles(currentAndChildRoleIds);
    }

    public PermissionsBuilder create(List<Integer> currentAndChildRoleIds, Integer targetRoleId) {
        return create(currentAndChildRoleIds).withTargetRole(targetRoleId);
    }
}
