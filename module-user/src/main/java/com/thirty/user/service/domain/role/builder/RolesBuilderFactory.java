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

    /**
     * 创建新的角色列表构建器实例（包含所有角色）
     */
    public RolesBuilder createWithAll() {
        return create()
                .includeAllRoles();
    }

    /**
     * 创建新的角色列表构建器实例（包含全局角色）
     */
    public RolesBuilder createWithGlobal() {
        return create()
                .includeUserRoles();
    }

    /**
     * 创建新的角色列表构建器实例（包含子角色）
     */
    public RolesBuilder createWithChild(Integer userId) {
        return create(userId)
                .includeChildRoles();
    }

    /**
     * 创建新的角色列表构建器实例（包含用户角色）
     */
    public RolesBuilder createWithUser(Integer userId) {
        return create(userId)
                .includeUserRoles();
    }

    /**
     * 创建新的角色列表构建器实例（包含子角色和全局角色）
     */
    public RolesBuilder createWithChildAndGlobal(Integer userId) {
        return create(userId)
                .includeGlobalRoles()
                .includeChildRoles();
    }

    /**
     * 创建新的角色列表构建器实例（包含子角色和用户角色）
     */
    public RolesBuilder createWithChildAndUser(Integer userId) {
        return create(userId)
                .includeUserRoles()
                .includeChildRoles();
    }
}
