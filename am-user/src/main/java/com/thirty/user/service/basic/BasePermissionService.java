package com.thirty.user.service.basic;

import com.baomidou.mybatisplus.extension.service.IService;
import com.thirty.user.model.entity.base.BasePermission;

import java.util.List;

/**
 * 基础权限服务接口
 * @param <T> 基础权限实体类型
 */
public interface BasePermissionService<T extends BasePermission> extends IService<T> {
    /**
     * 获取指定权限ID的所有祖先权限ID（不包含当前权限）
     * @param permissionId 权限ID
     * @return 所有祖先权限ID列表
     */
    List<Integer> getAncestorIds(Integer permissionId);

    /**
     * 获取指定权限ID列表的所有祖先权限ID（不包含当前权限）
     * @param permissionIds 权限ID列表
     * @return 所有祖先权限ID列表
     */
    List<Integer> getAncestorIds(List<Integer> permissionIds);

    /**
     * 获取指定权限ID的所有后代权限ID
     * @param permissionId 权限ID
     * @return 所有后代权限ID列表
     */
    List<Integer> getDescendantIds(Integer permissionId);

    /**
     * 获取指定权限ID列表的所有后代权限ID
     * @param permissionIds 权限ID列表
     * @return 所有后代权限ID列表
     */
    List<Integer> getDescendantIds(List<Integer> permissionIds);

     /**
      * 获取指定权限ID列表的所有权限码
      * @param permissionIds 权限ID列表
      * @return 所有权限码列表
      */
    List<String> getPermissionCodes(List<Integer> permissionIds);

     /**
      * 在指定权限之后插入权限
      * @param permission 权限实体
      * @param frontPermission 前一个权限实体
      */
    void insert(T permission, T frontPermission);

     /**
      * 在指定父节点的前头插入权限
      * @param permission 权限实体
      */
    void headInsert(T permission);

    /**
     * 尾插法插入权限
     * @param permission 权限实体
     */
    void tailInsert(T permission);

     /**
      * 连接权限的邻居节点
      * @param permission 权限实体
      */
    void connectNeighborPermissions(T permission);

     /**
      * 修改权限
      * @param permission 权限实体
      */
    void modifyPermission(T permission);

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
}
