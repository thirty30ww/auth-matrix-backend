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

    @Override
    public List<RoleVO> getRoleTree(String currentUsername) {
        return roleQueryDomain.getRoleTree(currentUsername, settingApi.hasPermissionDisplay());
    }

    @Override
    public List<Role> getRoles(String currentUsername, boolean isChild) {
        return isChild ? roleQueryDomain.getChildRoles(currentUsername) : roleQueryDomain.getRoles();
    }

    @Override
    public List<Role> getGlobalRoles() {
        return roleQueryDomain.getGlobalRoles();
    }
}
