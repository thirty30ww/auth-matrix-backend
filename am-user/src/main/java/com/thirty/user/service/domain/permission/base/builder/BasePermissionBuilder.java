package com.thirty.user.service.domain.permission.base.builder;

import com.thirty.user.enums.model.PermissionType;
import com.thirty.user.model.entity.base.BasePermission;
import com.thirty.user.service.basic.BasePermissionService;
import com.thirty.user.service.domain.permission.base.BasePermissionQueryDomain;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class BasePermissionBuilder<T extends BasePermission, E extends PermissionType> {

    final BasePermissionService<T> permissionService;
    final BasePermissionQueryDomain permissionQueryDomain;

    public BasePermissionBuilder(BasePermissionService<T> permissionService, BasePermissionQueryDomain permissionQueryDomain) {
        this.permissionService = permissionService;
        this.permissionQueryDomain = permissionQueryDomain;
    }

    List<Integer> currentRoleIds;
    Integer targetRoleId;
    String keyword;

    boolean filterPermission = false;
    boolean setChangeFlag = false;
    boolean setPermissionFlag = false;


}
