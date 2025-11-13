package com.thirty.user.converter;

import com.thirty.user.model.dto.RoleDTO;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.entity.RolePermission;
import com.thirty.user.model.vo.RoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface RoleConverter {
    RoleConverter INSTANCE = Mappers.getMapper(RoleConverter.class);

    /**
     * 将RoleDTO对象转换为Role对象
     */
    Role toRole(RoleDTO roleDTO);

    /**
     * 将Role对象转换为RoleVO对象
     */
    @Mapping(source = ".", target = "node")
    RoleVO toRoleVO(Role role);

    /**
     * 将Role列表转换为RoleVO列表
     */
    default List<RoleVO> toRoleVOS(List<Role> roles) {
        return roles.stream().map(this::toRoleVO).collect(Collectors.toList());
    }

    /**
     * 将角色id和权限id转换为角色权限对象
     * @param roleId 角色id
     * @param permissionId 权限id
     * @return 角色权限对象
     */
    RolePermission toRolePermission(Integer roleId, Integer permissionId);

    /**
     * 将角色id和权限id列表转换为角色权限对象列表
     * @param roleId 角色id
     * @param permissionIds 权限id列表
     * @return 角色权限对象列表
     */
    default List<RolePermission> toRolePermissions(Integer roleId, List<Integer> permissionIds) {
        return permissionIds.stream().map(viewId -> toRolePermission(roleId, viewId)).collect(Collectors.toList());
    }

    /**
     * 将角色id列表和权限id列表转换为角色权限对象列表
     * @param roleIds 角色id列表
     * @param viewIds 权限id列表
     * @return 角色权限对象列表
     */
    default List<RolePermission> toRolePermissions(List<Integer> roleIds, List<Integer> viewIds) {
        return roleIds.stream().flatMap(roleId -> toRolePermissions(roleId, viewIds).stream()).collect(Collectors.toList());
    }
}
