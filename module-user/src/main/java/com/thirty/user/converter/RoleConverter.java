package com.thirty.user.converter;

import com.thirty.user.model.dto.RoleDTO;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.entity.RoleView;
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
     * 将角色id和视图id转换为角色视图对象
     * @param roleId 角色id
     * @param viewId 视图id
     * @return 角色视图对象
     */
    RoleView toRoleView(Integer roleId, Integer viewId);

    /**
     * 将角色id和视图id列表转换为角色视图对象列表
     * @param roleId 角色id
     * @param viewIds 视图id列表
     * @return 角色视图对象列表
     */
    default List<RoleView> toRoleViews(Integer roleId, List<Integer> viewIds) {
        return viewIds.stream().map(viewId -> toRoleView(roleId, viewId)).collect(Collectors.toList());
    }

    /**
     * 将角色id列表和视图id列表转换为角色视图对象列表
     * @param roleIds 角色id列表
     * @param viewIds 视图id列表
     * @return 角色视图对象列表
     */
    default List<RoleView> toRoleViews(List<Integer> roleIds, List<Integer> viewIds) {
        return roleIds.stream().flatMap(roleId -> toRoleViews(roleId, viewIds).stream()).collect(Collectors.toList());
    }
}
