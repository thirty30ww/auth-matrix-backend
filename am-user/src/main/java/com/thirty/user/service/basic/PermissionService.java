package com.thirty.user.service.basic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.thirty.user.enums.model.PermissionType;
import com.thirty.user.model.entity.Permission;
import com.thirty.user.model.vo.PermissionVO;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【view(页面表)】的数据库操作Service
* @createDate 2025-08-03 10:06:39
*/
public interface PermissionService extends IService<Permission> {

    /**
     * 根据类型和名称获取权限列表
     * @param type 权限类型
     * @param keyword 权限名称
     * @return 权限列表
     */
    List<Permission> getPermissionByTypeAndKeyword(PermissionType type, String keyword);

    /**
     * 根据类型列表和名称获取权限列表
     * @param types 权限类型列表
     * @param keyword 权限名称
     * @return 权限列表
     */
    List<Permission> getPermissionByTypesAndKeyword(List<PermissionType> types, String keyword);

    /**
     * 根据查询条件获取权限列表
     * @param wrapper 查询条件
     * @return 权限列表
     */
    List<Permission> getPermissionByWrapper(LambdaQueryWrapper<Permission> wrapper);

    /**
     * 根据类型获取权限列表
     * @param type 权限类型
     * @return 权限列表
     */
    List<Permission> getPermissionByType(PermissionType type);

    /**
     * 根据类型列表获取权限列表
     * @param types 权限类型列表
     * @return 权限列表
     */
    List<Permission> getPermissionByTypes(List<PermissionType> types);

    /**
     * 根据类型和名称获取权限VO列表
     * @param type 权限类型
     * @param keyword 权限名称
     * @return 权限VO列表
     */
    List<PermissionVO> getPermissionVOByTypeAndKeyword(PermissionType type, String keyword);

    /**
     * 根据类型列表和名称获取权限VO列表
     * @param types 权限类型列表
     * @param keyword 权限名称
     * @return 权限VO列表
     */
    List<PermissionVO> getPermissionVOByTypesAndKeyword(List<PermissionType> types, String keyword);

    /**
     * 根据查询条件获取权限VO列表
     * @param wrapper 查询条件
     * @return 权限VO列表
     */
    List<PermissionVO> getPermissionVOByWrapper(LambdaQueryWrapper<Permission> wrapper);

    /**
     * 根据类型获取权限VO列表
     * @param type 权限类型
     * @return 权限VO列表
     */
    List<PermissionVO> getPermissionVOByType(PermissionType type);

    /**
     * 根据类型列表获取权限VO列表
     * @param types 权限类型列表
     * @return 权限VO列表
     */
    List<PermissionVO> getPermissionVOByTypes(List<PermissionType> types);

    /**
     * 获取权限的所有祖先ID（不包含当前权限）
     * @param viewId 权限ID
     * @return 祖先ID列表
     */
    List<Integer> getAncestorIds(Integer viewId);

    /**
     * 获取权限列表的所有祖先ID（不包含当前权限）
     * @param viewIds 权限ID列表
     * @return 祖先ID列表
     */
    List<Integer> getAncestorIds(List<Integer> viewIds);

    /**
     * 获取权限的所有后代ID（不包含当前权限）
     * @param viewId 权限ID
     * @return 后代ID列表
     */
    List<Integer> getDescendantIds(Integer viewId);

    /**
     * 获取权限列表的所有后代ID（不包含当前权限）
     * @param viewIds 权限ID列表
     * @return 后代ID列表
     */
    List<Integer> getDescendantIds(List<Integer> viewIds);

    /**
     * 获取权限列表的所有权限码
     * @param viewIds 权限ID列表
     * @return 权限码列表
     */
    List<String> getPermissionCodes(List<Integer> viewIds);

    /**
     * 修改权限
     * @param permission 权限
     */
    void modifyPermission(Permission permission);

    /**
     * 尾插法添加权限
     * @param permission 权限
     */
    void tailInsert(Permission permission);

    /**
     * 连接权限的邻居节点
     * @param permission 权限
     */
    void connectNeighborPermissions(Permission permission);

    /**
     * 删除权限
     * @param permissionId 权限ID
     */
    void deletePermission(Integer permissionId);

    /**
     * 权限上移
     * @param permissionId 权限ID
     */
    void moveUp(Integer permissionId);

    /**
     * 权限下移
     * @param permissionId 权限ID
     */
    void moveDown(Integer permissionId);

    /**
     * 获取权限的尾节点
     * @param parentId 父节点ID
     * @return 尾节点
     */
    Permission getTailNode(Integer parentId);

    /**
     * 获取权限的子节点
     * @param parentId 父节点ID
     * @return 子节点列表
     */
    List<Permission> getByParentId(Integer parentId);
}