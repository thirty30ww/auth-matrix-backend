package com.thirty.user.converter;

import com.thirty.user.model.dto.RoleDTO;
import com.thirty.user.model.entity.Role;
import org.mapstruct.factory.Mappers;

public interface RoleConverter {
    RoleConverter INSTANCE = Mappers.getMapper(RoleConverter.class);

    /**
     * 将RoleDTO对象转换为Role对象
     */
    Role toRole(RoleDTO roleDTO);
}
