package com.thirty.user.converter;

import com.thirty.user.model.dto.RoleDTO;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.entity.RoleView;
import org.mapstruct.Mapper;
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
}
