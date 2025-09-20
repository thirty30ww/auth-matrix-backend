package com.thirty.user.service.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
     * 根据类型和名称获取视图列表
     * @param type 视图类型
     * @param keyword 视图名称
     * @return 视图列表
     */
    List<Permission> getPermissionByTypeAndKeyword(PermissionType type, String keyword);

    /**
     * 根据类型列表和名称获取视图列表
     * @param types 视图类型列表
     * @param keyword 视图名称
     * @return 视图列表
     */
    List<Permission> getPermissionByTypesAndKeyword(List<PermissionType> types, String keyword);

    /**
     * 根据查询条件获取视图列表
     * @param wrapper 查询条件
     * @return 视图列表
     */
    List<Permission> getPermissionByWrapper(QueryWrapper<Permission> wrapper);

    /**
     * 根据类型获取视图列表
     * @param type 视图类型
     * @return 视图列表
     */
    List<Permission> getPermissionByType(PermissionType type);

    /**
     * 根据类型列表获取视图列表
     * @param types 视图类型列表
     * @return 视图列表
     */
    List<Permission> getPermissionByTypes(List<PermissionType> types);

    /**
     * 根据类型和名称获取视图VO列表
     * @param type 视图类型
     * @param keyword 视图名称
     * @return 视图VO列表
     */
    List<PermissionVO> getPermissionVOByTypeAndKeyword(PermissionType type, String keyword);

    /**
     * 根据类型列表和名称获取视图VO列表
     * @param types 视图类型列表
     * @param keyword 视图名称
     * @return 视图VO列表
     */
    List<PermissionVO> getPermissionVOByTypesAndKeyword(List<PermissionType> types, String keyword);

    /**
     * 根据查询条件获取视图VO列表
     * @param wrapper 查询条件
     * @return 视图VO列表
     */
    List<PermissionVO> getPermissionVOByWrapper(QueryWrapper<Permission> wrapper);

    /**
     * 根据类型获取视图VO列表
     * @param type 视图类型
     * @return 视图VO列表
     */
    List<PermissionVO> getPermissionVOByType(PermissionType type);

    /**
     * 根据类型列表获取视图VO列表
     * @param types 视图类型列表
     * @return 视图VO列表
     */
    List<PermissionVO> getPermissionVOByTypes(List<PermissionType> types);

    /**
     * 获取视图的所有祖先ID（不包含当前视图）
     * @param viewId 视图ID
     * @return 祖先ID列表
     */
    List<Integer> getAncestorIds(Integer viewId);

    /**
     * 获取视图列表的所有祖先ID（不包含当前视图）
     * @param viewIds 视图ID列表
     * @return 祖先ID列表
     */
    List<Integer> getAncestorIds(List<Integer> viewIds);

    /**
     * 获取视图的所有后代ID（不包含当前视图）
     * @param viewId 视图ID
     * @return 后代ID列表
     */
    List<Integer> getDescendantIds(Integer viewId);

    /**
     * 获取视图列表的所有后代ID（不包含当前视图）
     * @param viewIds 视图ID列表
     * @return 后代ID列表
     */
    List<Integer> getDescendantIds(List<Integer> viewIds);

    /**
     * 获取视图列表的所有权限码
     * @param viewIds 视图ID列表
     * @return 权限码列表
     */
    List<String> getPermissionCodes(List<Integer> viewIds);

    /**
     * 修改视图
     * @param permission 视图
     */
    void modifyPermission(Permission permission);

    /**
     * 尾插法添加视图
     * @param permission 视图
     */
    void tailInsert(Permission permission);

    /**
     * 连接视图的邻居节点
     * @param permission 视图
     */
    void connectNeighborPermissions(Permission permission);

    /**
     * 删除视图
     * @param permissionId 视图ID
     */
    void deletePermission(Integer permissionId);

    /**
     * 视图上移
     * @param permissionId 视图ID
     */
    void moveUp(Integer permissionId);

    /**
     * 视图下移
     * @param permissionId 视图ID
     */
    void moveDown(Integer permissionId);

    /**
     * 获取视图的尾节点
     * @param parentId 父节点ID
     * @return 尾节点
     */
    Permission getTailNode(Integer parentId);

    /**
     * 获取视图的子节点
     * @param parentId 父节点ID
     * @return 子节点列表
     */
    List<Permission> getByParentId(Integer parentId);
}
