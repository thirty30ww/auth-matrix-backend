package com.thirty.user.service.facade.impl;

import com.thirty.common.api.SettingApi;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.vo.RoleVO;
import com.thirty.user.service.domain.RoleQueryDomain;
import com.thirty.user.service.facade.RoleFacade;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleFacadeImpl implements RoleFacade {

    @Resource
    private RoleQueryDomain roleQueryDomain;

    @Resource
    private SettingApi settingApi;

    /**
     * 获取角色树
     * @param currentUserId 当前用户ID
     * @return 角色树
     */
    @Override
    public List<RoleVO> getRoleTree(Integer currentUserId) {
        return roleQueryDomain.getRoleTree(currentUserId, settingApi.hasPermissionDisplay());
    }

    /**
     * 获取角色列表
     * @param currentUserId 当前用户ID
     * @param isChild 是否获取子角色
     * @return 角色列表
     */
    @Override
    public List<Role> getRoles(Integer currentUserId, boolean isChild) {
        return isChild ? roleQueryDomain.getChildRoles(currentUserId) : roleQueryDomain.getRoles();
    }

    /**
     * 获取全局角色列表
     * @return 全局角色列表
     */
    @Override
    public List<Role> getGlobalRoles() {
        return roleQueryDomain.getGlobalRoles();
    }
}
