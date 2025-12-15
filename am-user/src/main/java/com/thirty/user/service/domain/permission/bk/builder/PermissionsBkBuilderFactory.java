package com.thirty.user.service.domain.permission.bk.builder;

import com.thirty.user.service.basic.PermissionBkService;
import com.thirty.user.service.domain.permission.bk.PermissionBkQueryDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionsBkBuilderFactory {
    @Resource
    private PermissionBkService permissionBkService;
    @Resource
    private PermissionBkQueryDomain permissionBkQueryDomain;

    public PermissionBkBuilder create() {
        return new PermissionBkBuilder(permissionBkService, permissionBkQueryDomain);
    }

    public PermissionBkBuilder create(List<Integer> currentAndChildRoleIds) {
        return create().forRoles(currentAndChildRoleIds);
    }

    public PermissionBkBuilder create(List<Integer> currentAndChildRoleIds, Integer targetRoleId) {
        return create(currentAndChildRoleIds).withTargetRole(targetRoleId);
    }
}
