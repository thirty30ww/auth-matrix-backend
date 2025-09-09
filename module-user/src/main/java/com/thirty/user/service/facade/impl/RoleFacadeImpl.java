package com.thirty.user.service.facade.impl;

import com.thirty.common.exception.BusinessException;
import com.thirty.system.api.SettingApi;
import com.thirty.user.enums.model.RolesType;
import com.thirty.user.enums.result.RoleResultCode;
import com.thirty.user.enums.result.ViewResultCode;
import com.thirty.user.model.dto.AssignViewDTO;
import com.thirty.user.model.dto.RoleDTO;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.vo.RoleVO;
import com.thirty.user.service.domain.role.RoleOperationDomain;
import com.thirty.user.service.domain.role.builder.RoleValidationBuilderFactory;
import com.thirty.user.service.domain.role.builder.RolesBuilderFactory;
import com.thirty.user.service.domain.view.ViewQueryDomain;
import com.thirty.user.service.domain.view.ViewValidationDomain;
import com.thirty.user.service.facade.RoleFacade;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleFacadeImpl implements RoleFacade {

    @Resource
    private RoleOperationDomain roleOperationDomain;
    @Resource
    private ViewQueryDomain viewQueryDomain;
    @Resource
    private ViewValidationDomain viewValidationDomain;

    @Resource
    private RolesBuilderFactory rolesBuilderFactory;
    @Resource
    private RoleValidationBuilderFactory roleValidationBuilderFactory;

    @Resource
    private SettingApi settingApi;

    /**
     * 获取角色树
     * @param currentUserId 当前用户ID
     * @param type 类型
     * @return 角色树
     */
    @Override
    public List<RoleVO> getRoleTree(Integer currentUserId, RolesType type) {
        return switch (type) {
            case ALL -> rolesBuilderFactory.createWithAll().buildTree();
            case CHILD -> rolesBuilderFactory.createWithChild(currentUserId).buildTree();
            case CHILD_AND_GLOBAL -> rolesBuilderFactory.createWithChildAndGlobal(currentUserId).buildTree();
            case CHILD_AND_SELF -> rolesBuilderFactory.createWithChildAndUser(currentUserId).buildTree();
            case NOT_GLOBAL -> {
                List<Integer> permittedRoleIds = rolesBuilderFactory.createWithChildAndGlobal(currentUserId).buildIds();
                yield rolesBuilderFactory.create().excludeGlobalRoles()
                        .buildTree(settingApi.hasPermissionDisplay(), permittedRoleIds);
            }
            default -> new ArrayList<>();
        };
    }

    /**
     * 获取角色列表
     * @param currentUserId 当前用户ID
     * @param type 类型
     * @return 角色列表
     */
    @Override
    public List<Role> getRoles(Integer currentUserId, RolesType type) {
        return switch (type) {
            case ALL -> rolesBuilderFactory.createWithAll().build();
            case CHILD -> rolesBuilderFactory.createWithChild(currentUserId).build();
            case CHILD_AND_GLOBAL -> rolesBuilderFactory.createWithChildAndGlobal(currentUserId).build();
            case GLOBAL -> rolesBuilderFactory.createWithGlobal().build();
            case CHILD_AND_SELF -> rolesBuilderFactory.createWithChildAndUser(currentUserId).build();
            case NOT_GLOBAL -> new ArrayList<>();
        };
    }

    /**
     * 添加角色
     * @param roleDTO 角色dto
     */
    @Override
    public void addRole(RoleDTO roleDTO, Integer userId) {
        if (!roleValidationBuilderFactory.createWithChildAndUser(userId)
                .validateRole(roleDTO.getParentNodeId())) {
            throw new BusinessException(RoleResultCode.ROLE_NOT_AUTHORIZED_ADD);
        }
        roleOperationDomain.addRole(roleDTO);
    }

    /**
     * 修改角色
     * @param roleDTO 角色dto
     * @param userId 用户ID
     */
    @Override
    public void updateRole(RoleDTO roleDTO, Integer userId) {
        if (!roleValidationBuilderFactory.createWithChildAndGlobal(userId).validateRole(roleDTO.getId())) {
            throw new BusinessException(RoleResultCode.ROLE_NOT_AUTHORIZED_UPDATE);
        }
        if (!roleValidationBuilderFactory.createWithChildAndUser(userId)
                .validateRole(roleDTO.getParentNodeId())) {
            throw new BusinessException(RoleResultCode.ROLE_NOT_AUTHORIZED_UPDATE);
        }
        roleOperationDomain.updateRole(roleDTO);
    }

    /**
     * 删除角色
     * @param roleId 角色ID
     * @param userId 用户ID
     */
    @Override
    public void deleteRole(Integer roleId, Integer userId) {
        if (!roleValidationBuilderFactory.createWithChildAndGlobal(userId).validateRole(roleId)) {
            throw new BusinessException(RoleResultCode.ROLE_NOT_AUTHORIZED_DELETE);
        }
        roleOperationDomain.deleteRole(roleId);
    }

    /**
     * 分配视图权限
     * @param userId 当前操作用户ID
     * @param assignViewDTO 分配视图dto
     */
    @Override
    public void assignView(Integer userId, AssignViewDTO assignViewDTO) {
        Integer targetRoleId = assignViewDTO.getRoleId();
        List<Integer> newViewIds = assignViewDTO.getViewIds();

        if (!roleValidationBuilderFactory.createWithChildAndGlobal(userId).validateRole(targetRoleId)) {
            throw new BusinessException(RoleResultCode.ROLE_NOT_AUTHORIZED_ASSIGN);
        }
        if (!viewValidationDomain.validateViewContainUserViews(userId, newViewIds)) {
            throw new BusinessException(ViewResultCode.VIEW_NOT_AUTHORIZED_ASSIGN);
        }

        List<Integer> oldViewIds = viewQueryDomain.getPermissionId(targetRoleId);
        roleOperationDomain.assignView(targetRoleId, oldViewIds, newViewIds);
    }
}
