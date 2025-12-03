package com.thirty.user.service.domain.permission.bk.impl;

import com.thirty.user.constant.RoleConstant;
import com.thirty.user.enums.model.PermissionType;
import com.thirty.user.model.entity.PermissionBk;
import com.thirty.user.service.basic.PermissionBkService;
import com.thirty.user.service.basic.RoleService;
import com.thirty.user.service.basic.RolePermissionBkService;
import com.thirty.user.service.domain.permission.bk.PermissionBkQueryDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PermissionBkQueryDomainImpl implements PermissionBkQueryDomain {

    @Resource
    private PermissionBkService permissionBkService;
    @Resource
    private RolePermissionBkService rolePermissionBkService;
    @Resource
    private RoleService roleService;

    /**
     * 获取菜单id列表
     * @param roleId 角色id
     * @return 菜单id列表
     */
    @Override
    public List<Integer> getPermissionId(Integer roleId) {
        return getPermissionId(List.of(roleId));
    }

    /**
     * 获取菜单id列表
     * @param roleIds 角色id列表
     * @return 菜单id列表
     */
    @Override
    public List<Integer> getPermissionId(List<Integer> roleIds) {
        // 获取最高级角色
        Integer highestLevel = roleService.getHighestLevel(roleIds);
        if (Objects.equals(highestLevel, RoleConstant.ROLE_HIGHEST_LEVEL)) {
            // 返回所有菜单
            return PermissionBk.extractViewIds(permissionBkService.getPermissionByTypes(List.of(
                    PermissionType.DIRECTORY,
                    PermissionType.MENU,
                    PermissionType.BUTTON
            )));
        }
        // 否则返回角色菜单列表
        return rolePermissionBkService.getPermissionIds(roleIds);
    }


    /**
     * 获取权限码列表
     * @param roleIds 角色id列表
     * @return 权限码列表
     */
    @Override
    public List<String> getPermissionCode(List<Integer> roleIds) {
        List<Integer> permissionId = getPermissionId(roleIds);
        return permissionBkService.getPermissionCodes(permissionId);
    }


}
