package com.thirty.user.converter;

import com.thirty.user.enums.model.PermissionType;
import com.thirty.user.model.dto.PermissionDTO;
import com.thirty.user.model.entity.Permission;
import com.thirty.user.model.vo.PermissionVO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * View实体与DTO之间的转换器
 */
@Mapper
public interface PermissionConverter {
    PermissionConverter INSTANCE = Mappers.getMapper(PermissionConverter.class);
    
    /**
     * 将Permission对象转换为PermissionVO对象
     * @param permission Permission对象
     * @return PermissionVO对象
     */
    @Mapping(source = ".", target = "node")
    PermissionVO toPermissionVO(Permission permission);
    
    /**
     * 将Permission列表转换为PermissionVO列表
     * @param permissions Permission列表
     * @return PermissionVO列表
     */
    List<PermissionVO> toPermissionVOS(List<Permission> permissions);

    /**
     * 将PermissionDTO对象转换为Permission对象
     * @param permissionDTO PermissionDTO对象
     * @return Permission对象
     */
    Permission toPermission(PermissionDTO permissionDTO);

    /**
     * 转换后处理：当type为PAGE时，自动设置hasPermission为true
     */
    @AfterMapping
    default void afterMappingToPermissionVO(@MappingTarget PermissionVO permissionVO, Permission permission) {
        if (permission.getType() == PermissionType.PAGE) {
            permissionVO.setHasPermission(true);
        }
    }
}