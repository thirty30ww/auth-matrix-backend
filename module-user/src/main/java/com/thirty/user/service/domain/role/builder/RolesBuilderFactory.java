package com.thirty.user.service.domain.role.builder;

import com.thirty.user.service.basic.RoleService;
import com.thirty.user.service.basic.UserRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RolesBuilderFactory {
    @Resource
    private RoleService roleService;
    @Resource
    private UserRoleService userRoleService;

    /**
     * 创建新的角色列表构建器实例
     */
    public RolesBuilder create() {
        return new RolesBuilder(roleService, userRoleService);
    }

    /**
     * 创建新的角色列表构建器实例（指定用户）
     */
    public RolesBuilder create(Integer userId) {
        return create()
                .forUser(userId);
    }
}
