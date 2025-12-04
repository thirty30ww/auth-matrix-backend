package com.thirty.user.service.basic.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thirty.user.converter.PermissionBkConverter;
import com.thirty.user.enums.model.PermissionBkType;
import com.thirty.user.mapper.PermissionBkMapper;
import com.thirty.user.model.entity.PermissionBk;
import com.thirty.user.model.vo.PermissionBkVO;
import com.thirty.user.service.basic.PermissionBkService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
* @author Lenovo
* @description 针对表【view(页面表)】的数据库操作Service实现
* @createDate 2025-08-03 10:06:39
*/
@Service
public class PermissionBkServiceImpl extends BasePermissionServiceImpl<PermissionBkMapper, PermissionBk>
    implements PermissionBkService {

    /**
     * 根据类型和名称获取权限列表
     * @param type 权限类型
     * @param keyword 权限名称
     * @return 权限列表
     */
    @Override
    public List<PermissionBk> getPermissionByTypeAndKeyword(PermissionBkType type, String keyword) {
        LambdaQueryWrapper<PermissionBk> wrapper = new LambdaQueryWrapper<>();
        if (type != null) {
            wrapper.eq(PermissionBk::getType, type);
        }
        if (keyword != null) {
            wrapper.like(PermissionBk::getName, keyword);
        }
        return list(wrapper);
    }

    /**
     * 根据类型列表和名称获取权限列表
     * @param types 权限类型列表
     * @param keyword 权限名称
     * @return 权限列表
     */
    @Override
    public List<PermissionBk> getPermissionByTypesAndKeyword(List<PermissionBkType> types, String keyword) {
        LambdaQueryWrapper<PermissionBk> wrapper = new LambdaQueryWrapper<>();
        if (CollectionUtils.isEmpty(types)) {
            return Collections.emptyList();
        }
        wrapper.in(PermissionBk::getType, types)
                .orderByAsc(PermissionBk::getFrontId);
        if (keyword != null) {
            wrapper.like(PermissionBk::getName, keyword);
        }
        return list(wrapper);
    }

    /**
     * 根据类型获取权限列表
     * @param type 权限类型
     * @return 权限列表
     */
    @Override
    public List<PermissionBk> getPermissionByType(PermissionBkType type) {
        return getPermissionByTypeAndKeyword(type, null);
    }

    /**
     * 根据类型列表获取权限列表
     * @param types 权限类型列表
     * @return 权限列表
     */
    @Override
    public List<PermissionBk> getPermissionByTypes(List<PermissionBkType> types) {
        return getPermissionByTypesAndKeyword(types, null);
    }

    /**
     * 根据类型列表和名称获取权限VO列表
     * @param types 权限类型列表
     * @param keyword 权限名称
     * @return 权限VO列表
     */
    @Override
    public List<PermissionBkVO> getPermissionVOByTypesAndKeyword(List<PermissionBkType> types, String keyword) {
        List<PermissionBk> permissionBks = getPermissionByTypesAndKeyword(types, keyword);
        return PermissionBkConverter.INSTANCE.toPermissionVOS(permissionBks);
    }
}