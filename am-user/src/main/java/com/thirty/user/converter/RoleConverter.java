package com.thirty.user.converter;

import com.thirty.user.model.dto.RoleDTO;
import com.thirty.user.model.entity.Role;
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
}
