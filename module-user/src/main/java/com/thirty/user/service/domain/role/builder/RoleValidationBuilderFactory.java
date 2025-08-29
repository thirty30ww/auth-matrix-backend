package com.thirty.user.service.domain.role.builder;

import com.thirty.user.service.basic.UserRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 角色验证构建器工厂
 */
@Service
public class RoleValidationBuilderFactory {
    
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private RoleListBuilderFactory roleListBuilderFactory;
    
    /**
     * 创建新的角色验证构建器实例
     */
    public RoleValidationBuilder create() {
        return new RoleValidationBuilder(userRoleService, roleListBuilderFactory);
    }

    /**
     * 创建新的角色验证构建器实例（包括子角色和全局角色）
     */
    public RoleValidationBuilder createWithChildAndGlobal(Integer userId) {
        return create()
                .forUser(userId)
                .includeChildRoles()
                .includeGlobalRoles();
    }
}