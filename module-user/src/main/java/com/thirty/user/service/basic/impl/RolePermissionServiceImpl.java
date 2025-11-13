package com.thirty.user.service.basic.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thirty.user.converter.RoleConverter;
import com.thirty.user.mapper.RolePermissionMapper;
import com.thirty.user.model.entity.RolePermission;
import com.thirty.user.service.basic.RolePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
* @author Lenovo
* @description 针对表【role_view(角色页面表)】的数据库操作Service实现
* @createDate 2025-08-22 14:21:16
*/
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission>
    implements RolePermissionService {

    /**
     * 获取权限id列表
     * @param roleId 角色id
     * @return 权限id列表
     */
    @Override
    public List<Integer> getPermissionIds(Integer roleId) {
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermission::getRoleId, roleId);
        return RolePermission.extractPermissionIds(list(queryWrapper));
    }

    /**
     * 获取权限id列表
     * @param roleIds 角色id列表
     * @return 权限id列表
     */
    @Override
    public List<Integer> getPermissionIds(List<Integer> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RolePermission::getRoleId, roleIds);

        return RolePermission.extractPermissionIds(list(queryWrapper));
    }

    /**
     * 根据角色删除角色权限
     * @param roleId 角色id
     */
    @Override
    public void deleteByRoleId(Integer roleId) {
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermission::getRoleId, roleId);
        remove(queryWrapper);
    }
    /**
     * 根据权限删除角色权限
     * @param permissionId 权限id
     */
    @Override
    public void deleteByPermissionId(Integer permissionId) {
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermission::getPermissionId, permissionId);
        remove(queryWrapper);
    }

    /**
     * 获取存在的角色权限
     * @param roleIds 角色id列表
     * @param permissionIds 权限id列表
     * @return 存在的角色权限列表
     */
    @Override
    public List<String> getExists(List<Integer> roleIds, List<Integer> permissionIds) {
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RolePermission::getRoleId, roleIds).in(RolePermission::getPermissionId, permissionIds);
        return RolePermission.spliceRolePermissionIds(list(queryWrapper));
    }

    /**
     * 添加角色权限
     * @param roleId 角色id
     * @param permissionIds 权限id列表
     */
    @Override
    public void addRolePermissions(Integer roleId, List<Integer> permissionIds) {
        List<RolePermission> rolePermissions = RoleConverter.INSTANCE.toRolePermissions(roleId, permissionIds);
        saveBatch(rolePermissions);
    }

    /**
     * 添加角色权限
     * @param roleIds 角色id列表
     * @param permissionIds 权限id列表
     */
    @Override
    public void addRolePermissions(List<Integer> roleIds, List<Integer> permissionIds) {
        if (CollectionUtils.isEmpty(roleIds) || CollectionUtils.isEmpty(permissionIds)) {
            return;
        }

        List<RolePermission> rolePermissions = RoleConverter.INSTANCE.toRolePermissions(roleIds, permissionIds);
        List<String> exists = getExists(roleIds, permissionIds);
        rolePermissions.removeIf(rv -> exists.contains(rv.getRoleId() + "_" + rv.getPermissionId()));

        saveBatch(rolePermissions);
    }

    /**
     * 删除角色权限
     * @param roleId 角色id
     * @param permissionIds 权限id列表
     */
    @Override
    public void deleteRolePermissions(Integer roleId, List<Integer> permissionIds) {
        if (roleId == null || CollectionUtils.isEmpty(permissionIds)) {
            return;
        }

        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermission::getRoleId, roleId);
        queryWrapper.in(RolePermission::getPermissionId, permissionIds);
        remove(queryWrapper);
    }

    /**
     * 删除角色权限
     * @param roleIds 角色id列表
     * @param permissionIds 权限id列表
     */
    @Override
    public void deleteRolePermissions(List<Integer> roleIds, List<Integer> permissionIds) {
        if (CollectionUtils.isEmpty(roleIds) || CollectionUtils.isEmpty(permissionIds)) {
            return;
        }

        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RolePermission::getRoleId, roleIds);
        queryWrapper.in(RolePermission::getPermissionId, permissionIds);
        remove(queryWrapper);
    }
}