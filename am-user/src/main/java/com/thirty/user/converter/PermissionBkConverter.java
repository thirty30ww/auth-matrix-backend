package com.thirty.user.converter;

import com.thirty.user.enums.model.PermissionBkType;
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
 * PermissionBk转换接口
 */
@Mapper
public interface PermissionBkConverter {
    PermissionBkConverter INSTANCE = Mappers.getMapper(PermissionBkConverter.class);
    
    /**
     * 将PermissionBk对象转换为PermissionVO对象
     * @param permissionBk PermissionBk对象
     * @return PermissionVO对象
     */
    @Mapping(source = ".", target = "node")
    PermissionBkVO toPermissionVO(PermissionBk permissionBk);
    
    /**
     * 将PermissionBk列表转换为PermissionVO列表
     * @param permissionBks PermissionBk列表
     * @return PermissionVO列表
     */
    List<PermissionBkVO> toPermissionVOS(List<PermissionBk> permissionBks);

    /**
     * 将PermissionBkDTO对象转换为PermissionBk对象
     * @param permissionBkDTO PermissionBkDTO对象
     * @return PermissionBk对象
     */
    PermissionBk toPermission(PermissionBkDTO permissionBkDTO);

    /**
     * 转换后处理：当type为PAGE时，自动设置hasPermission为true
     */
    @AfterMapping
    default void afterMappingToPermissionVO(@MappingTarget PermissionBkVO permissionBkVO, PermissionBk permissionBk) {
        if (permissionBk.getType() == PermissionBkType.PAGE) {
            permissionBkVO.setHasPermission(true);
        }
    }
}