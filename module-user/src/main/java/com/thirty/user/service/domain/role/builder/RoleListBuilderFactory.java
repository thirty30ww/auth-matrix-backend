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
     * 创建新的角色列表构建器实例（包含子角色和全局角色）
     */
    public RoleListBuilder createWithChildAndGlobal(Integer userId) {
        return create()
                .forUser(userId)
                .includeGlobalRoles()
                .includeChildRoles();
    }
}
