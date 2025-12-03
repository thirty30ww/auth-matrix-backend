package com.thirty.user.converter;

import com.thirty.user.enums.model.PermissionType;
import com.thirty.user.model.dto.PermissionBkDTO;
import com.thirty.user.model.entity.PermissionBk;
import com.thirty.user.model.vo.PermissionBkVO;
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
public interface PermissionBkConverter {
    PermissionBkConverter INSTANCE = Mappers.getMapper(PermissionBkConverter.class);
    
    /**
     * 将Permission对象转换为PermissionVO对象
     * @param permissionBk Permission对象
     * @return PermissionVO对象
     */
    @Mapping(source = ".", target = "node")
    PermissionBkVO toPermissionVO(PermissionBk permissionBk);
    
    /**
     * 将Permission列表转换为PermissionVO列表
     * @param permissionBks Permission列表
     * @return PermissionVO列表
     */
    List<PermissionBkVO> toPermissionVOS(List<PermissionBk> permissionBks);

    /**
     * 将PermissionDTO对象转换为Permission对象
     * @param permissionBkDTO PermissionDTO对象
     * @return Permission对象
     */
    PermissionBk toPermission(PermissionBkDTO permissionBkDTO);

    /**
     * 转换后处理：当type为PAGE时，自动设置hasPermission为true
     */
    @AfterMapping
    default void afterMappingToPermissionVO(@MappingTarget PermissionBkVO permissionBkVO, PermissionBk permissionBk) {
        if (permissionBk.getType() == PermissionType.PAGE) {
            permissionBkVO.setHasPermission(true);
        }
    }
}