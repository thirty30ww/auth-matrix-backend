package com.thirty.user.service.basic;

import com.baomidou.mybatisplus.extension.service.IService;
import com.thirty.user.enums.model.PermissionBkType;
import com.thirty.user.model.entity.PermissionBk;
import com.thirty.user.model.vo.PermissionBkVO;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【view(页面表)】的数据库操作Service
* @createDate 2025-08-03 10:06:39
*/
public interface PermissionBkService extends IService<PermissionBk> {

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

    /**
     * 获取权限的所有祖先ID（不包含当前权限）
     * @param permissionId 权限ID
     * @return 祖先ID列表
     */
    List<Integer> getAncestorIds(Integer permissionId);

    /**
     * 获取权限列表的所有祖先ID（不包含当前权限）
     * @param permissionIds 权限ID列表
     * @return 祖先ID列表
     */
    List<Integer> getAncestorIds(List<Integer> permissionIds);

    /**
     * 获取权限的所有后代ID（不包含当前权限）
     * @param permissionId 权限ID
     * @return 后代ID列表
     */
    List<Integer> getDescendantIds(Integer permissionId);

    /**
     * 获取权限列表的所有后代ID（不包含当前权限）
     * @param permissionIds 权限ID列表
     * @return 后代ID列表
     */
    List<Integer> getDescendantIds(List<Integer> permissionIds);

    /**
     * 获取权限列表的所有权限码
     * @param permissionIds 权限ID列表
     * @return 权限码列表
     */
    List<String> getPermissionCodes(List<Integer> permissionIds);

    /**
     * 修改权限
     * @param permissionBk 权限
     */
    void modifyPermission(PermissionBk permissionBk);

    /**
     * 尾插法添加权限
     * @param permissionBk 权限
     */
    void tailInsert(PermissionBk permissionBk);

    /**
     * 连接权限的邻居节点
     * @param permissionBk 权限
     */
    void connectNeighborPermissions(PermissionBk permissionBk);

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
    PermissionBk getTailNode(Integer parentId);

    /**
     * 获取权限的子节点
     * @param parentId 父节点ID
     * @return 子节点列表
     */
    List<PermissionBk> getByParentId(Integer parentId);
}