package com.thirty.user.service.domain.permission.builder;

import com.thirty.common.utils.TreeBuilder;
import com.thirty.user.constant.RoleConstant;
import com.thirty.user.enums.model.PermissionType;
import com.thirty.user.model.vo.PermissionVO;
import com.thirty.user.service.basic.PermissionService;
import com.thirty.user.service.domain.permission.PermissionQueryDomain;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.util.CollectionUtils;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE) // 所有字段默认私有
public class PermissionsBuilder {

    final PermissionService permissionService;
    final PermissionQueryDomain permissionQueryDomain;

    public PermissionsBuilder(PermissionService permissionService, PermissionQueryDomain permissionQueryDomain) {
        this.permissionService = permissionService;
        this.permissionQueryDomain = permissionQueryDomain;
    }

    List<Integer> currentRoleIds;
    Integer targetRoleId;
    String keyword;

    List<PermissionType> permissionTypes;
    boolean filterPermission = false;
    boolean setChangeFlag = false;
    boolean setPermissionFlag = false;

    /**
     * 设置搜索关键词
     */
    public PermissionsBuilder withKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    /**
     * 设置权限列表
     */
    public PermissionsBuilder withPermissionTypes(List<PermissionType> permissionTypes) {
        this.permissionTypes = permissionTypes;
        return this;
    }

    /**
     * 设置当前角色ID列表
     */
    public PermissionsBuilder forRoles(List<Integer> currentRoleIds) {
        this.currentRoleIds = currentRoleIds;
        return this;
    }

    /**
     * 设置目标角色ID
     */
    public PermissionsBuilder withTargetRole(Integer targetRoleId) {
        this.targetRoleId = targetRoleId;
        return this;
    }

    /**
     * 过滤权限（只返回有权限的权限）
     */
    public PermissionsBuilder filterByPermission() {
        this.filterPermission = true;
        return this;
    }

    /**
     * 设置变更标志（为每个权限设置hasChange属性）
     */
    public PermissionsBuilder withChangeFlag() {
        this.setChangeFlag = true;
        return this;
    }

    /**
     * 设置权限标志（为每个权限设置hasPermission属性）
     */
    public PermissionsBuilder withPermissionFlag() {
        this.setPermissionFlag = true;
        return this;
    }

    /**
     * 构建成列表（平铺结构）
     */
    public List<PermissionVO> build() {
        List<PermissionVO> permissionVOS = permissionService.getPermissionVOByTypesAndKeyword(permissionTypes, keyword);
        return processPermissions(permissionVOS);
    }

    /**
     * 构建成树结构
     */
    public List<PermissionVO> buildTree() {
        List<PermissionVO> permissionVOS = build();
        return getTreeByParentNodeId(permissionVOS);
    }

    /**
     * 处理权限相关逻辑
     *
     * @param permissionVOS 权限VO列表
     * @return 处理后的权限VO列表
     */
    private List<PermissionVO> processPermissions(List<PermissionVO> permissionVOS) {
        if (CollectionUtils.isEmpty(currentRoleIds)) {
            return permissionVOS;
        }

        // 获取当前角色的权限权限ID
        List<Integer> currentPermissionIds = permissionQueryDomain.getPermissionId(currentRoleIds);

        // 过滤权限
        if (filterPermission) {
            PermissionVO.filterHasPermission(currentPermissionIds, permissionVOS);
        } else if (setChangeFlag) {
            PermissionVO.setHasChange(currentPermissionIds, permissionVOS);
        }

        // 设置权限标志
        if (setPermissionFlag) {
            if (targetRoleId != null) {
                // 目标角色的权限权限ID
                List<Integer> targetPermissionIds = permissionQueryDomain.getPermissionId(targetRoleId);
                PermissionVO.setHasPermission(targetPermissionIds, permissionVOS);
            } else {
                PermissionVO.setHasPermission(currentPermissionIds, permissionVOS);
            }
        }

        return permissionVOS;
    }

    /**
     * 根据父节点ID构建树
     */
    private List<PermissionVO> getTreeByParentNodeId(List<PermissionVO> permissions) {
        TreeBuilder<PermissionVO, Integer> treeBuilder = new TreeBuilder<>();

        // 构建树结构
        return treeBuilder.buildTree(
                permissions,
                permissionVO -> permissionVO.getNode().getId(),
                permissionVO -> permissionVO.getNode().getParentNodeId(),
                PermissionVO::getChildren,
                RoleConstant.ROOT_ROLE_PARENT_ID
        );
    }
}
