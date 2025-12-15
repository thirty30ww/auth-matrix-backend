package com.thirty.user.service.domain.permission.bk.builder;

import com.thirty.common.utils.TreeBuilder;
import com.thirty.user.constant.PermissionConstant;
import com.thirty.user.constant.RoleConstant;
import com.thirty.user.converter.PermissionBkConverter;
import com.thirty.user.enums.model.PermissionBkType;
import com.thirty.user.model.entity.PermissionBk;
import com.thirty.user.model.vo.PermissionBkVO;
import com.thirty.user.service.basic.PermissionBkService;
import com.thirty.user.service.domain.permission.bk.PermissionBkQueryDomain;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE) // 所有字段默认私有
public class PermissionBkBuilder {

    final PermissionBkService permissionBkService;
    final PermissionBkQueryDomain permissionBkQueryDomain;

    public PermissionBkBuilder(PermissionBkService permissionBkService, PermissionBkQueryDomain permissionBkQueryDomain) {
        this.permissionBkService = permissionBkService;
        this.permissionBkQueryDomain = permissionBkQueryDomain;
    }

    List<Integer> currentRoleIds;
    Integer targetRoleId;
    String keyword;

    List<PermissionBkType> permissionBkTypes;
    boolean filterPermission = false;
    boolean setChangeFlag = false;
    boolean setPermissionFlag = false;

    /**
     * 设置搜索关键词
     */
    public PermissionBkBuilder withKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    /**
     * 设置权限列表
     */
    public PermissionBkBuilder withPermissionTypes(List<PermissionBkType> permissionBkTypes) {
        this.permissionBkTypes = permissionBkTypes;
        return this;
    }

    /**
     * 设置当前角色ID列表
     */
    public PermissionBkBuilder forRoles(List<Integer> currentRoleIds) {
        this.currentRoleIds = currentRoleIds;
        return this;
    }

    /**
     * 设置目标角色ID
     */
    public PermissionBkBuilder withTargetRole(Integer targetRoleId) {
        this.targetRoleId = targetRoleId;
        return this;
    }

    /**
     * 过滤权限（只返回有权限的权限）
     */
    public PermissionBkBuilder filterByPermission() {
        this.filterPermission = true;
        return this;
    }

    /**
     * 设置变更标志（为每个权限设置hasChange属性）
     */
    public PermissionBkBuilder withChangeFlag() {
        this.setChangeFlag = true;
        return this;
    }

    /**
     * 设置权限标志（为每个权限设置hasPermission属性）
     */
    public PermissionBkBuilder withPermissionFlag() {
        this.setPermissionFlag = true;
        return this;
    }

    /**
     * 构建成列表（平铺结构）
     */
    public List<PermissionBkVO> build() {
        List<PermissionBk> permissionBks = permissionBkService.getPermissionByTypesAndKeyword(permissionBkTypes, keyword);
        List<PermissionBkVO> permissionBkVOS = PermissionBkConverter.INSTANCE.toPermissionVOS(permissionBks);
        return processPermissions(permissionBkVOS);
    }

    /**
     * 构建成树结构
     */
    public List<PermissionBkVO> buildTree() {
        List<PermissionBkVO> permissionBkVOS = build();
        return getTreeByParentNodeId(permissionBkVOS);
    }

    /**
     * 处理权限相关逻辑
     *
     * @param permissionBkVOS 权限VO列表
     * @return 处理后的权限VO列表
     */
    private List<PermissionBkVO> processPermissions(List<PermissionBkVO> permissionBkVOS) {
        if (CollectionUtils.isEmpty(currentRoleIds)) {
            return permissionBkVOS;
        }

        // 获取当前角色的权限权限ID
        List<Integer> currentPermissionIds = permissionBkQueryDomain.getPermissionId(currentRoleIds);

        // 过滤权限
        if (filterPermission) {
            PermissionBkVO.filterHasPermission(currentPermissionIds, permissionBkVOS);
        } else if (setChangeFlag) {
            PermissionBkVO.setHasChange(currentPermissionIds, permissionBkVOS);
        }

        // 设置权限标志
        if (setPermissionFlag) {
            if (targetRoleId != null) {
                // 目标角色的权限权限ID
                List<Integer> targetPermissionIds = permissionBkQueryDomain.getPermissionId(targetRoleId);
                PermissionBkVO.setHasPermission(targetPermissionIds, permissionBkVOS);
            } else {
                PermissionBkVO.setHasPermission(currentPermissionIds, permissionBkVOS);
            }
        }

        return permissionBkVOS;
    }

    /**
     * 根据父节点ID构建树
     */
    private List<PermissionBkVO> getTreeByParentNodeId(List<PermissionBkVO> permissions) {
        TreeBuilder<PermissionBkVO, Integer> treeBuilder = new TreeBuilder<>();

        // 构建树结构
        List<PermissionBkVO> resultTree = treeBuilder.buildTree(
                permissions,
                permissionVO -> permissionVO.getNode().getId(),
                permissionVO -> permissionVO.getNode().getParentId(),
                PermissionBkVO::getChildren,
                RoleConstant.ROOT_ROLE_PARENT_ID,
                this::sortSiblings
        );

        // 提取页面类型的权限
        resultTree.addAll(PermissionBkVO.extractType(permissions, PermissionBkType.PAGE));
        return resultTree;
    }

    /**
     * 对同父节点的权限列表进行排序
     * @param permissions 同父节点的权限列表
     * @return 排序后的权限列表
     */
    private List<PermissionBkVO> sortSiblings(List<PermissionBkVO> permissions) {
        if (CollectionUtils.isEmpty(permissions)) {
            return permissions;
        }

        // 构建id到权限VO的映射
        Map<Integer, PermissionBkVO> permissionMap = PermissionBkVO.buildMap(permissions);

        // 获取头权限id，作为当前节点id
        Integer currentId = PermissionBkVO.getHeadPermissionId(permissions);

        List<PermissionBkVO> result = new ArrayList<>();
        // 遍历链表，将权限VO添加到结果列表中，直到遇到尾节点
        while (!Objects.equals(currentId, PermissionConstant.TAIL_PERMISSION_BEHIND_ID)) {
            PermissionBkVO currentPermission = permissionMap.get(currentId);

            if (currentPermission == null) {
                break;
            }

            result.add(currentPermission);

            if (Objects.equals(currentPermission.getNode().getBehindId(), PermissionConstant.TAIL_PERMISSION_BEHIND_ID)) {
                break;
            }

            currentId = currentPermission.getNode().getBehindId();
        }

        return result;
    }
}
