package com.thirty.user.service.basic.impl;

import com.thirty.user.mapper.RolePermissionBkMapper;
import com.thirty.user.model.entity.RolePermissionBk;
import com.thirty.user.service.basic.RolePermissionBkService;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【role_view(角色页面表)】的数据库操作Service实现
* @createDate 2025-08-22 14:21:16
*/
@Service
public class RolePermissionBkServiceImpl extends BaseRolePermissionServiceImpl<RolePermissionBkMapper, RolePermissionBk>
    implements RolePermissionBkService {
}