package com.thirty.user.service.domain.permission.builder;

import com.thirty.user.constant.RoleConstant;
import com.thirty.user.enums.model.PermissionType;
import com.thirty.user.model.vo.PermissionVO;
import com.thirty.user.service.basic.PermissionService;
import com.thirty.user.service.domain.permission.PermissionQueryDomain;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
     * 过滤权限（只返回有权限的视图）
     */
    public PermissionsBuilder filterByPermission() {
        this.filterPermission = true;
        return this;
    }

    /**
     * 设置变更标志（为每个视图设置hasChange属性）
     */
    public PermissionsBuilder withChangeFlag() {
        this.setChangeFlag = true;
        return this;
    }

    /**
     * 设置权限标志（为每个视图设置hasPermission属性）
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
        return getTreeByParentNodeId(permissionVOS, RoleConstant.ROOT_ROLE_PARENT_ID);
    }

    /**
     * 处理权限相关逻辑
     */
    private List<PermissionVO> processPermissions(List<PermissionVO> permissionVOS) {
        if (CollectionUtils.isEmpty(currentRoleIds)) {
            return permissionVOS;
        }

        // 获取当前角色的权限视图ID
        List<Integer> currentViewIds = permissionQueryDomain.getPermissionId(currentRoleIds);

        // 过滤权限
        if (filterPermission) {
            PermissionVO.filterHasPermission(currentViewIds, permissionVOS);
        } else if (setChangeFlag) {
            PermissionVO.setHasChange(currentViewIds, permissionVOS);
        }

        // 设置权限标志
        if (setPermissionFlag) {
            if (targetRoleId != null) {
                // 目标角色的权限视图ID
                List<Integer> targetViewIds = permissionQueryDomain.getPermissionId(targetRoleId);
                PermissionVO.setHasPermission(targetViewIds, permissionVOS);
            } else {
                PermissionVO.setHasPermission(currentViewIds, permissionVOS);
            }
        }

        return permissionVOS;
    }

    /**
     * 根据父节点ID构建树
     */
    private List<PermissionVO> getTreeByParentNodeId(List<PermissionVO> permissions, Integer parentNodeId) {
        // 获取同一个父节点的ViewVO列表并且按FrontNodeId的从小到大排序
        List<PermissionVO> permissionVOS = PermissionVO.sortByFrontNodeId(permissions, parentNodeId);

        // 记录返回结果
        List<PermissionVO> responses = new ArrayList<>();

        // 遍历排序后的ViewVO列表
        permissionVOS.forEach(viewVO -> {
            // 递归构建子树
            viewVO.setChildren(getTreeByParentNodeId(permissions, viewVO.getNode().getId()));
            // 添加到返回结果
            responses.add(viewVO);
        });

        return responses;
    }
}
