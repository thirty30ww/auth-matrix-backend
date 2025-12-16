package com.thirty.user.service.domain.permission.base.builder;

import com.thirty.user.constant.PermissionConstant;
import com.thirty.user.enums.model.PermissionType;
import com.thirty.user.model.entity.base.BasePermission;
import com.thirty.user.model.vo.base.BasePermissionVO;
import com.thirty.user.service.basic.BasePermissionService;
import com.thirty.user.service.domain.permission.base.BasePermissionQueryDomain;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BasePermissionBuilder<
        T extends BasePermission,
        VO extends BasePermissionVO<T, VO>,
        E extends PermissionType
        > {

    final BasePermissionService<T> permissionService;
    final BasePermissionQueryDomain permissionQueryDomain;

    public BasePermissionBuilder(BasePermissionService<T> permissionService, BasePermissionQueryDomain permissionQueryDomain) {
        this.permissionService = permissionService;
        this.permissionQueryDomain = permissionQueryDomain;
    }

    List<Integer> currentRoleIds;
    Integer targetRoleId;
    String keyword;

    List<E> permissionTypes;
    boolean filterPermission = false;
    boolean setChangeFlag = false;
    boolean setPermissionFlag = false;

    /**
     * 设置搜索关键词
     * @param keyword 搜索关键词
     * @return 当前构建器实例
     */
    public BasePermissionBuilder withKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

     /**
     * 设置权限类型列表
     * @param permissionTypes 权限类型列表
     * @return 当前构建器实例
     */
    public BasePermissionBuilder withPermissionTypes(List<E> permissionTypes) {
        this.permissionTypes = permissionTypes;
        return this;
    }

     /**
     * 设置当前角色ID列表
     * @param roleIds 当前角色ID列表
     * @return 当前构建器实例
     */
    public BasePermissionBuilder forRoles(List<Integer> roleIds) {
        this.currentRoleIds = roleIds;
        return this;
    }

     /**
     * 设置目标角色ID
     * @param targetRoleId 目标角色ID
     * @return 当前构建器实例
     */
    public BasePermissionBuilder withTargetRole(Integer targetRoleId) {
        this.targetRoleId = targetRoleId;
        return this;
    }

     /**
     * 过滤权限（只返回有权限的权限）
     * @return 当前构建器实例
     */
    public BasePermissionBuilder filterByPermission() {
        this.filterPermission = true;
        return this;
    }

     /**
     * 设置权限标志（为每个权限设置hasPermission属性）
     * @return 当前构建器实例
     */
    public BasePermissionBuilder withPermissionFlag() {
        this.setPermissionFlag = true;
        return this;
    }

     /**
     * 设置变更标志（为每个权限设置hasChange属性）
     * @return 当前构建器实例
     */
    public BasePermissionBuilder withChangeFlag() {
        this.setChangeFlag = true;
        return this;
    }

    /**
     * 处理权限VO列表，根据当前角色ID列表和目标角色ID设置hasChange和hasPermission属性
     * @param permissionVOS 权限VO列表
     * @return 处理后的权限VO列表
     */
    private List<VO> processPermissions(List<VO> permissionVOS) {
        if (CollectionUtils.isEmpty(currentRoleIds)) {
            return permissionVOS;
        }

        // 获取当前角色的所有权限ID
        List<Integer> currentPermissionIds = permissionQueryDomain.getPermissionId(currentRoleIds);

        if (filterPermission) { // 过滤出当前角色有权限的权限
            filterHasPermission(currentPermissionIds, permissionVOS);
        } else if (setChangeFlag) { // 为每个权限设置hasChange属性
            BasePermissionVO.setHasChange(currentPermissionIds, permissionVOS);
        }

        // 设置hasPermission属性
        if (setPermissionFlag) {
            if (targetRoleId != null) {
                // 目标角色有权限的ID列表
                List<Integer> targetPermissionIds = permissionQueryDomain.getPermissionId(targetRoleId);
                // 为每个权限设置hasPermission属性
                BasePermissionVO.setHasPermission(targetPermissionIds, permissionVOS);
            } else {
                BasePermissionVO.setHasPermission(currentPermissionIds, permissionVOS);
            }
        }

        return permissionVOS;
    }

    /**
     * 对权限VO列表进行排序，根据节点的behindId属性构建链表结构
     * @param permissions 权限VO列表
     * @return 排序后的权限VO列表
     */
    private List<VO> sortSiblings(List<VO> permissions) {
        if (CollectionUtils.isEmpty(permissions)) { return permissions; }   // 空列表直接返回

        Map<Integer, VO> permissionMap = VO.buildMap(permissions);  // 构建权限ID到权限VO的映射

        Integer currentId = VO.getHeadPermissionId(permissions);  // 获取头权限的id作为当前节点id
        List<VO> result = new ArrayList<>();  // 用于存储排序后的权限VO列表

        // 遍历链表，将权限VO添加到结果列表中，直到遇到尾节点
        while (!Objects.equals(currentId, PermissionConstant.TAIL_PERMISSION_BEHIND_ID)) {
            VO currentPermission = permissionMap.get(currentId);

            if (currentPermission == null) {    // 若当前节点不存在，则跳出循环
                break;
            }

            result.add(currentPermission);  // 将当前节点添加到结果列表中

            currentId = currentPermission.getNode().getBehindId();  // 移动到下一个节点
        }
        return result;
    }

    /**
     * 过滤出当前角色有的权限
     * @param permittedPermissionIds 当前角色有的权限ID列表
     * @param permissionVOS 权限VO列表
     */
    protected abstract void filterHasPermission(List<Integer> permittedPermissionIds, List<VO> permissionVOS);
}
