package com.thirty.user.service.basic;

import com.thirty.user.model.entity.RolePermissionBk;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【role_view(角色页面表)】的数据库操作Service
* @createDate 2025-08-22 14:21:16
*/
public interface RolePermissionBkService extends IService<RolePermissionBk> {
    /**
     * 获取权限id列表
     * @param roleId 角色id
     * @return 权限id列表
     */
    List<Integer> getPermissionIds(Integer roleId);

    /**
     * 获取权限id列表
     * @param roleIds 角色id列表
     * @return 权限id列表
     */
    List<Integer> getPermissionIds(List<Integer> roleIds);

    /**
     * 根据角色删除角色权限
     * @param roleId 角色id
     */
    void deleteByRoleId(Integer roleId);

    /**
     * 根据权限删除角色权限
     * @param permissionId 权限id
     */
    void deleteByPermissionId(Integer permissionId);

    /**
     * 获取存在的角色权限
     * @param roleIds 角色id列表
     * @param permissionIds 权限id列表
     * @return 存在的角色权限列表
     */
    List<String> getExists(List<Integer> roleIds, List<Integer> permissionIds);

    /**
     * 添加角色权限
     * @param roleId 角色id
     * @param permissionIds 权限id列表
     */
    void addRolePermissions(Integer roleId, List<Integer> permissionIds);

    /**
     * 添加角色权限
     * @param roleIds 角色id列表
     * @param permissionIds 权限id列表
     */
    void addRolePermissions(List<Integer> roleIds, List<Integer> permissionIds);

    /**
     * 删除角色权限
     * @param roleId 角色id
     * @param permissionIds 权限id列表
     */
    void deleteRolePermissions(Integer roleId, List<Integer> permissionIds);

    /**
     * 删除角色权限
     * @param roleIds 角色id列表
     * @param permissionIds 权限id列表
     */
    void deleteRolePermissions(List<Integer> roleIds, List<Integer> permissionIds);
}
