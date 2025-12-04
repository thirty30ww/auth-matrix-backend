package com.thirty.user.service.basic;

import com.thirty.user.enums.model.PermissionBkType;
import com.thirty.user.model.entity.PermissionBk;
import com.thirty.user.model.vo.PermissionBkVO;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【view(页面表)】的数据库操作Service
* @createDate 2025-08-03 10:06:39
*/
public interface PermissionBkService extends BasePermissionService<PermissionBk> {

    /**
     * 根据类型和名称获取权限列表
     * @param type 权限类型
     * @param keyword 权限名称
     * @return 权限列表
     */
    List<PermissionBk> getPermissionByTypeAndKeyword(PermissionBkType type, String keyword);

    /**
     * 根据类型列表和名称获取权限列表
     * @param types 权限类型列表
     * @param keyword 权限名称
     * @return 权限列表
     */
    List<PermissionBk> getPermissionByTypesAndKeyword(List<PermissionBkType> types, String keyword);

    /**
     * 根据类型获取权限列表
     * @param type 权限类型
     * @return 权限列表
     */
    List<PermissionBk> getPermissionByType(PermissionBkType type);

    /**
     * 根据类型列表获取权限列表
     * @param types 权限类型列表
     * @return 权限列表
     */
    List<PermissionBk> getPermissionByTypes(List<PermissionBkType> types);

    /**
     * 根据类型列表和名称获取权限VO列表
     * @param types 权限类型列表
     * @param keyword 权限名称
     * @return 权限VO列表
     */
    List<PermissionBkVO> getPermissionVOByTypesAndKeyword(List<PermissionBkType> types, String keyword);
}