package com.thirty.user.service.domain.role.builder;

import com.thirty.user.service.domain.role.RoleQueryDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RoleListBuilderFactory {
    @Resource
    private RoleQueryDomain roleQueryDomain;

    /**
     * 创建新的角色列表构建器实例
     */
    public RoleListBuilder create() {
        return new RoleListBuilder(roleQueryDomain);
    }

    /**
     * 创建新的角色列表构建器实例（指定用户）
     */
    public RoleListBuilder create(Integer userId) {
        return create()
                .forUser(userId);
    }

    /**
     * 创建新的角色列表构建器实例（包含所有角色）
     */
    public RoleListBuilder createWithAll() {
        return create()
                .includeAllRoles();
    }

    /**
     * 创建新的角色列表构建器实例（包含全局角色）
     */
    public RoleListBuilder createWithGlobal() {
        return create()
                .includeGlobalRoles();
    }

    /**
     * 创建新的角色列表构建器实例（包含子角色）
     */
    public RoleListBuilder createWithChild(Integer userId) {
        return create(userId)
                .includeChildRoles();
    }


    /**
     * 创建新的角色列表构建器实例（包含子角色和全局角色）
     */
    public RoleListBuilder createWithChildAndGlobal(Integer userId) {
        return create(userId)
                .includeGlobalRoles()
                .includeChildRoles();
    }

    /**
     * 创建新的角色列表构建器实例（包含子角色和用户角色）
     */
    public RoleListBuilder createWithChildAndUser(Integer userId) {
        return create(userId)
                .includeUserRoles()
                .includeChildRoles();
    }
}
